package pdytr.example.grpc.client;

import com.google.protobuf.ByteString;
import io.grpc.*;
import pdytr.example.grpc.ServiceGrpc;
import pdytr.example.grpc.ServiceOuterClass;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:8080")
                .usePlaintext(true)
                .build();

        ServiceGrpc.ServiceBlockingStub stub = ServiceGrpc.newBlockingStub(
                channel
        );

        ServiceOuterClass.Request request;

        int timeout;

        for (int i = 1; i <= 10; i++) {
            timeout = getRandomTimeout();
            System.out.println("Cliente --> Realiza un pedido al servidor con deadline de "+timeout+" segundos (pedido " + i + "/10)");
            request = ServiceOuterClass.Request.newBuilder().setId(i).setTimeout(timeout).build();
            try {
                stub.withDeadlineAfter(timeout , TimeUnit.SECONDS).hacerAlgoConTimeout(request);
            } catch (io.grpc.StatusRuntimeException e) {
                printError(e);
            }
            Thread.sleep(2000);
        }

        channel.shutdownNow();
    }

    private static int getRandomTimeout(){
        return ((int)(Math.random() * ((20 - 5) + 1)) + 5 );
    }
    private static void printError(io.grpc.StatusRuntimeException e) {
        if (e.getStatus().getCode() == Status.DEADLINE_EXCEEDED.getCode())
            System.out.println("Cliente --> " + e.getMessage());
        System.out.println("Cliente --> Error manejado -> se vencio el deadline establecido");
        //System.out.println("Cliente --> Se puede capturar la excepcion StatusRuntimeException que se arroja al vencerse el deadline y hacer algo al respecto");
    }

}
