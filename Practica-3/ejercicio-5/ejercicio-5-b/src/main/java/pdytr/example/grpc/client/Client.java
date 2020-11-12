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
        
        for (int i = 1; i <= 10; i++) {
            System.out.println("Cliente --> Realiza un pedido al servidor con timeout de 459000ns segundos (pedido " + i + "/10)");
            try {
                stub.withDeadlineAfter(459000 , TimeUnit.NANOSECONDS).hacerAlgo(null);
            } catch (io.grpc.StatusRuntimeException e) {
                printError(e);
            }
            //se duerme solo para darle tiempo al servidor y que no se defase todo cuando imprimen
            Thread.sleep(1000);
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
