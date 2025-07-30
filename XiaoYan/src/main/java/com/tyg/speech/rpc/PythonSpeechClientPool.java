package com.tyg.speech.rpc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PythonSpeechClientPool {
    private static final int MAX_POOL_SIZE = 10;
    private static final BlockingQueue<PythonSpeechClient> pool =
            new ArrayBlockingQueue<>(MAX_POOL_SIZE);

    public static  PythonSpeechClient getClient(String host, int port) {
        PythonSpeechClient client = pool.poll();
        if (client == null) {
            client = new PythonSpeechClient(host, port);
        }
        return client;
    }

    public static void returnClient(PythonSpeechClient client) throws InterruptedException {
        if (!pool.offer(client)) {
            client.shutdown();
        }
    }


}