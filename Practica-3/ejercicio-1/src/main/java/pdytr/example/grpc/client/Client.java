package pdytr.example.grpc.client;

import com.google.protobuf.ByteString;
import io.grpc.*;
import pdytr.example.grpc.ServiceGrpc;
import pdytr.example.grpc.ServiceOuterClass;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Client {
    private static ServiceGrpc.ServiceBlockingStub stub;
    private static ServiceGrpc.ServiceStub asyncStub;
    public static void main(String[] args) throws Exception {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:8080")
                .usePlaintext(true)
                .build();

        stub = ServiceGrpc.newBlockingStub(
                channel
        );
        asyncStub = ServiceGrpc.newStub(channel);

        if(args.length > 0){
            String operacion=args[0];
            switch (operacion){
                case "op1":
                {
                    operacion1();
                    break;
                }
                case "op2":
                case "op3":{
                    operacion2();
                    break;
                }
                case "op4":{
                    operacion4();
                    break;
                }
                case "op5":{
                    operacion5();
                    break;
                }
            }
        }
        channel.shutdownNow();
    }

    private static void operacion1() throws InterruptedException{
        System.out.println("Cliente --> crea un hilo");
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Cliente --> llama a operacion1");
                stub.operacion1(null);
                System.out.println("Cliente --> termine la operacion");
            }
        };
        thread.start();
        Thread.sleep(2000);
        System.out.println("Cliente --> interrumpe al hilo");
        thread.interrupt();
    }

    private static void operacion2(){
        System.out.println("Cliente --> llama a operacion2");
        stub.operacion2(null);
    }

    private static void operacion4(){
        System.out.println("Cliente --> llama a operacion4");
        stub.operacion4(null);
    }

    private static void operacion5() throws InterruptedException{
        System.out.println("Cliente --> crea un hilo");
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Cliente --> llama a operacion1");
                stub.operacion1(null);
                System.out.println("Cliente --> termine la operacion");
            }
        };
        thread.start();
        Thread.sleep(2000);
        System.out.println("Cliente --> detengo el programa");
        System.exit(-1);
    }
}
