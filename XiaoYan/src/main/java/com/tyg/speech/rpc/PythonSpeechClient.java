package com.tyg.speech.rpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PythonSpeechClient {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ManagedChannel channel;
    private final SpeechGrpc.SpeechStub asyncStub;

    public PythonSpeechClient(String host, int port) {
        log.info("init python client：{}:{}",host,port);

//        channel = ManagedChannelBuilder.forAddress(host,port)
//                .usePlaintext()
//                .build();

        EventLoopGroup group = new NioEventLoopGroup();
        channel = NettyChannelBuilder.forAddress(host,port)
                .usePlaintext()
                .channelType(NioSocketChannel.class)
                .eventLoopGroup(group)
                .build();
        asyncStub = SpeechGrpc.newStub(channel);
    }
    public String recognize(byte[] audioData, int sampleRate) {
        SpeechProto.SpeechRequest req = SpeechProto.SpeechRequest.newBuilder()
                .setAudioData(com.google.protobuf.ByteString.copyFrom(audioData))
                .setSampleRate(sampleRate)
                .build();
        SpeechProto.SpeechResponse resp = SpeechGrpc.newBlockingStub(channel).recognize(req);
        return resp.getText();
    }
    /**
     * 流式识别：audioStream 里每片音频都会被包装成 SpeechRequest，
     * 每收到一条 SpeechResponse 就回调 onResult。
     */
    public void recognizeStreaming(
            BlockingQueue<byte[]> audioStream,
            int sampleRate,
            Consumer<SpeechProto.SpeechResponse> onResult) throws InterruptedException {

        CountDownLatch finish = new CountDownLatch(1);

        StreamObserver<SpeechProto.SpeechRequest> requestObserver =
                asyncStub.streamingRecognize(new StreamObserver<>() {
                    @Override public void onNext(SpeechProto.SpeechResponse value) {
                        onResult.accept(value);
                    }
                    @Override public void onError(Throwable t) {
                        log.error("streaming error", t);
                        finish.countDown();
                    }
                    @Override public void onCompleted() {
                        log.info("streaming completed");
                        finish.countDown();
                    }
                });

        // 音频数据生产线程
        try {
            while (true) {
                byte[] chunk = audioStream.poll(50, TimeUnit.MILLISECONDS);
                if (chunk == null) continue;
                if (chunk.length == 0) break; // 约定空包代表结束

                SpeechProto.SpeechRequest req = SpeechProto.SpeechRequest.newBuilder()
                        .setAudioData(com.google.protobuf.ByteString.copyFrom(chunk))
                        .setSampleRate(sampleRate)
                        .setInterimResults(true)   // 让服务端给中间结果
                        .build();
                requestObserver.onNext(req);
            }
        } finally {
            requestObserver.onCompleted();
            finish.await();
        }
    }

    public void shutdown() {
        channel.shutdownNow();
    }
}