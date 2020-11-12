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
    public void operacion1(
            ServiceOuterClass.Request request,
            StreamObserver<ServiceOuterClass.Response> responseObserver
    ) {
        System.out.println("Servidor --> Operacion 1");
        try {
            System.out.println("Servidor --> Se duerme durante 5s");
            Thread.sleep(5000);
            System.out.println("Servidor --> Me desperte para seguir atendiendo al pedido");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void operacion2(
            ServiceOuterClass.Request request,
            StreamObserver<ServiceOuterClass.Response> responseObserver
    ) { }

    @Override
    public void operacion4(
            ServiceOuterClass.Request request,
            StreamObserver<ServiceOuterClass.Response> responseObserver
    ) {
        try {
            System.out.println("Servidor --> Operacion 4");
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        System.exit(1);
    }
}
