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

        if(args.length != 1){
            System.out.println("Debe indicar el tamaño de muestra.");
            return;
        }

        int sampleSize = Integer.parseInt(args[0]);

        System.out.println("Tamano de muestra: " + sampleSize);
        long start, end, diff;
        long[] samples = new long[sampleSize];
        double total = 0L;

        for (int i = 0; i < sampleSize; i++) {
            start = System.currentTimeMillis();
            stub.metodoInutil(null);
            end = System.currentTimeMillis();
            diff = end - start;
            samples[i] = diff;
            total = total + diff;
        }
        final double std = calcularDE(samples);
        System.out.println("Total: " + total + "ms");
        System.out.println("Promedio: " + total / sampleSize + "ms");
        System.out.println("Desviacion estandar: " + std + "ms");


        channel.shutdownNow();
    }

    private static double calcularDE(long numArray[]) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        for (long num : numArray) {
            sum += num;
        }
        double mean = sum / length;
        for (long num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation / length);
    }

}
