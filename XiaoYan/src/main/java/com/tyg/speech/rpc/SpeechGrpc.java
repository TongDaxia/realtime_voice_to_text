package com.tyg.speech.rpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.0)",
    comments = "Source: speech.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SpeechGrpc {

  private SpeechGrpc() {}

  public static final String SERVICE_NAME = "speech.Speech";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest,
      com.tyg.speech.rpc.SpeechProto.SpeechResponse> getRecognizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Recognize",
      requestType = com.tyg.speech.rpc.SpeechProto.SpeechRequest.class,
      responseType = com.tyg.speech.rpc.SpeechProto.SpeechResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest,
      com.tyg.speech.rpc.SpeechProto.SpeechResponse> getRecognizeMethod() {
    io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest, com.tyg.speech.rpc.SpeechProto.SpeechResponse> getRecognizeMethod;
    if ((getRecognizeMethod = SpeechGrpc.getRecognizeMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getRecognizeMethod = SpeechGrpc.getRecognizeMethod) == null) {
          SpeechGrpc.getRecognizeMethod = getRecognizeMethod =
              io.grpc.MethodDescriptor.<com.tyg.speech.rpc.SpeechProto.SpeechRequest, com.tyg.speech.rpc.SpeechProto.SpeechResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Recognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tyg.speech.rpc.SpeechProto.SpeechRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tyg.speech.rpc.SpeechProto.SpeechResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("Recognize"))
              .build();
        }
      }
    }
    return getRecognizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest,
      com.tyg.speech.rpc.SpeechProto.SpeechResponse> getStreamingRecognizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamingRecognize",
      requestType = com.tyg.speech.rpc.SpeechProto.SpeechRequest.class,
      responseType = com.tyg.speech.rpc.SpeechProto.SpeechResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest,
      com.tyg.speech.rpc.SpeechProto.SpeechResponse> getStreamingRecognizeMethod() {
    io.grpc.MethodDescriptor<com.tyg.speech.rpc.SpeechProto.SpeechRequest, com.tyg.speech.rpc.SpeechProto.SpeechResponse> getStreamingRecognizeMethod;
    if ((getStreamingRecognizeMethod = SpeechGrpc.getStreamingRecognizeMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getStreamingRecognizeMethod = SpeechGrpc.getStreamingRecognizeMethod) == null) {
          SpeechGrpc.getStreamingRecognizeMethod = getStreamingRecognizeMethod =
              io.grpc.MethodDescriptor.<com.tyg.speech.rpc.SpeechProto.SpeechRequest, com.tyg.speech.rpc.SpeechProto.SpeechResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamingRecognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tyg.speech.rpc.SpeechProto.SpeechRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tyg.speech.rpc.SpeechProto.SpeechResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("StreamingRecognize"))
              .build();
        }
      }
    }
    return getStreamingRecognizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SpeechStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SpeechStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SpeechStub>() {
        @java.lang.Override
        public SpeechStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SpeechStub(channel, callOptions);
        }
      };
    return SpeechStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SpeechBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SpeechBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SpeechBlockingStub>() {
        @java.lang.Override
        public SpeechBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SpeechBlockingStub(channel, callOptions);
        }
      };
    return SpeechBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SpeechFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SpeechFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SpeechFutureStub>() {
        @java.lang.Override
        public SpeechFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SpeechFutureStub(channel, callOptions);
        }
      };
    return SpeechFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void recognize(com.tyg.speech.rpc.SpeechProto.SpeechRequest request,
        io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecognizeMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamingRecognizeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Speech.
   */
  public static abstract class SpeechImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SpeechGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Speech.
   */
  public static final class SpeechStub
      extends io.grpc.stub.AbstractAsyncStub<SpeechStub> {
    private SpeechStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SpeechStub(channel, callOptions);
    }

    /**
     */
    public void recognize(com.tyg.speech.rpc.SpeechProto.SpeechRequest request,
        io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecognizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getStreamingRecognizeMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Speech.
   */
  public static final class SpeechBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SpeechBlockingStub> {
    private SpeechBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SpeechBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.tyg.speech.rpc.SpeechProto.SpeechResponse recognize(com.tyg.speech.rpc.SpeechProto.SpeechRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecognizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Speech.
   */
  public static final class SpeechFutureStub
      extends io.grpc.stub.AbstractFutureStub<SpeechFutureStub> {
    private SpeechFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SpeechFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tyg.speech.rpc.SpeechProto.SpeechResponse> recognize(
        com.tyg.speech.rpc.SpeechProto.SpeechRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecognizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECOGNIZE = 0;
  private static final int METHODID_STREAMING_RECOGNIZE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECOGNIZE:
          serviceImpl.recognize((com.tyg.speech.rpc.SpeechProto.SpeechRequest) request,
              (io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAMING_RECOGNIZE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamingRecognize(
              (io.grpc.stub.StreamObserver<com.tyg.speech.rpc.SpeechProto.SpeechResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRecognizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.tyg.speech.rpc.SpeechProto.SpeechRequest,
              com.tyg.speech.rpc.SpeechProto.SpeechResponse>(
                service, METHODID_RECOGNIZE)))
        .addMethod(
          getStreamingRecognizeMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.tyg.speech.rpc.SpeechProto.SpeechRequest,
              com.tyg.speech.rpc.SpeechProto.SpeechResponse>(
                service, METHODID_STREAMING_RECOGNIZE)))
        .build();
  }

  private static abstract class SpeechBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SpeechBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.tyg.speech.rpc.SpeechProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Speech");
    }
  }

  private static final class SpeechFileDescriptorSupplier
      extends SpeechBaseDescriptorSupplier {
    SpeechFileDescriptorSupplier() {}
  }

  private static final class SpeechMethodDescriptorSupplier
      extends SpeechBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SpeechMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SpeechGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SpeechFileDescriptorSupplier())
              .addMethod(getRecognizeMethod())
              .addMethod(getStreamingRecognizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
