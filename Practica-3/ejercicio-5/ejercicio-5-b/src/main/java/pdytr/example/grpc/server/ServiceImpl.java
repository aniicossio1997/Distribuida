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
        System.out.println("Servidor --> Atendiendo el pedido " + request.getId());
        //ponemos a dormir al proceso
        try {
            System.out.println("Servidor --> Me dormi mientras atendia el pedido "+request.getId()+"...");
            Thread.sleep(13000);
            System.out.println("Servidor --> Me desperte para seguir atendiendo al pedido "+request.getId()+"...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Context.current().isCancelled()) {
            System.out.println("Servidor --> El cliente cancelo la operacion " + request.getId());
            System.out.println("Servidor --> Puedo dejar de ejecutar el pedido " + request.getId());
            responseObserver.onError(Status.CANCELLED.withDescription("Cancelled by client").asRuntimeException());
            return;
        }

        ServiceOuterClass.Response response = ServiceOuterClass.Response.newBuilder().build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

}
