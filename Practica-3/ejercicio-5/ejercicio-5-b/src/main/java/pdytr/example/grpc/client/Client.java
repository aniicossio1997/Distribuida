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

        System.out.println("Cliente --> Realiza un pedido al servidor con deadline de 10 segundos");

        ServiceOuterClass.Request request = ServiceOuterClass.Request.newBuilder().setId(0).build();
        ServiceOuterClass.Response response;
        try {
            response = stub.withDeadlineAfter(10000, TimeUnit.MILLISECONDS).hacerAlgo(request);
            Thread.sleep(7000);
        } catch (io.grpc.StatusRuntimeException e) {
            printError(e);
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println("Cliente --> Realiza un pedido al servidor con deadline de 9 segundos (pedido " + i + "/10)");
            request = ServiceOuterClass.Request.newBuilder().setId(i).build();
            try {
                response = stub.withDeadlineAfter(9000, TimeUnit.MILLISECONDS).hacerAlgo(request);
            } catch (io.grpc.StatusRuntimeException e) {
                printError(e);
            }
            //se duerme solo para darle tiempo al servidor y que no se defase todo cuando imprimen
            Thread.sleep(5000);
        }

        channel.shutdownNow();
    }

    private static void printError(io.grpc.StatusRuntimeException e) {
        if (e.getStatus().getCode() == Status.DEADLINE_EXCEEDED.getCode())
            System.out.println("Cliente --> " + e.getMessage());
        System.out.println("Cliente --> Error manejado -> No hubo respuesta del servidor");
        //System.out.println("Cliente --> Se puede capturar la excepcion StatusRuntimeException que se arroja al vencerse el deadline y hacer algo al respecto");
    }

}
