package pdytr.example.grpc.server;

import com.google.protobuf.ByteString;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.ServiceGrpc;
import pdytr.example.grpc.ServiceOuterClass;

import java.io.*;

public class ServiceImpl extends ServiceGrpc.ServiceImplBase {



    @Override
    public void hacerAlgo(
            ServiceOuterClass.Request request,
            StreamObserver<ServiceOuterClass.Response> responseObserver
    ) {
      
        responseObserver.onNext(null);

        responseObserver.onCompleted();
    }

}
