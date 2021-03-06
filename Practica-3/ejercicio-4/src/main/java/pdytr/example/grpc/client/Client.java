package pdytr.example.grpc.client;

import com.google.protobuf.ByteString;
import io.grpc.*;
import pdytr.example.grpc.FileServiceGrpc;
import pdytr.example.grpc.FileServiceOuterClass;

import java.io.*;
import java.util.Iterator;

public class Client {

    private static FileServiceGrpc.FileServiceBlockingStub stub;

    private static int bufferSize = 32768;

    private static String clientId = "Cliente ";

    private static String filesFolder =
            "files/client/";

    public static void main(String[] args) throws Exception {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:8080")
                .usePlaintext(true)
                .build();

        stub = FileServiceGrpc.newBlockingStub(
                channel
        );

        if (args.length < 2) {
            System.out.println("Cliente -> Debe indicar la operacion a realizar (read/write)");
            System.out.println("Cliente -> Debe indicar el nombre del archivo.");
            System.exit(1);
        }

        String operation = args[0];
        String fileName = args[1];

        if(args.length > 2)
            clientId = clientId + args[2];

        System.out.println(clientId + " -> Nombre del archivo: "+fileName);

        System.out.println(clientId + " -> Operacion: "+operation);

        switch (operation){
            case "read":
                read(fileName);
                break;
            case"write":
                write(fileName);
                break;
            case "readServerStreaming":
                readServerStreaming(fileName);
                break;
            default:
                System.out.println(clientId + " --> Opearacion invalida");
        }

        channel.shutdownNow();
    }

    private static void read(String name) {
        String route = filesFolder + name;
        File file = new File(route);
        long offset = 0;
        FileServiceOuterClass.ReadResponse response = null;
        OutputStream output = null;
        try {
            FileServiceOuterClass.ReadRequest request =
                    FileServiceOuterClass.ReadRequest.newBuilder()
                            .setName(name)
                            .setPos(0)
                            .setCount(bufferSize).build();

            long startTimestamp = System.currentTimeMillis();

            response = stub.read(request);
            if (response.getCount() > -1) {

                if (file.createNewFile()) System.out.println(
                        clientId +" -> Se creo el archivo " + name
                );

                output = new FileOutputStream(file, true); // true para que no se pisen los datos
                output.write(response.getData().toByteArray(), 0, response.getCount());
                offset = offset + response.getCount();
            } else {
                System.out.println(
                        clientId +" -> No se encontro el archivo '" + name + "' en el servidor."
                );
            }
            while (response.getCount() > -1) {
                request = FileServiceOuterClass.ReadRequest.newBuilder(request)
                        .setCount(bufferSize)
                        .setPos((int)offset)
                        .build();

                response = stub.read(request);
                if (response.getCount() > -1) {
                    output.write(response.getData().toByteArray(), 0, response.getCount());
                    offset = offset + response.getCount();
                }
            }
            long endTimestamp = System.currentTimeMillis();
            if (offset > 0) {
                System.out.println(clientId +" -> Transferencia completada.");
                System.out.println(clientId +" -> Tiempo: "+(endTimestamp - startTimestamp)+"ms");
                output.close();
                return;
            }
        } catch (Exception e) {
            System.out.println(clientId +" -> Error al escribir el archivo.");
            e.printStackTrace();
        }
    }

    private static void write(String name) {
        final String ruta = filesFolder + name;
        try {
            File file = new File(ruta);
            if(!file.exists()){
                System.out.println(clientId +" -> No se encontro el archivo.");
                return;
            }
            InputStream inputStream = new FileInputStream(file);
            FileServiceOuterClass.WriteRequest request;
            request =
                    FileServiceOuterClass.WriteRequest.newBuilder()
                            .setName(name).build();

            int read = 0; // cantidad leida en un read
            byte[] buffer = new byte[bufferSize];
            while (read > -1) {

                read = inputStream.read(buffer, 0, bufferSize);
                if (read > 0) { // si no hay error ni se termino el archivo
                    request =
                            FileServiceOuterClass.WriteRequest.newBuilder(request)
                                    .setData(ByteString.copyFrom(buffer))
                                    .setCount(bufferSize).build();
                    stub.write(request);
                }
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println(clientId +" -> Se produjo un error.");
            e.printStackTrace();
            return;
        }
        System.out.println(clientId +" -> Transferencia completada exitosamente.");
    }

    private static void readServerStreaming(String name) {
        String route = filesFolder + name;
        File file = new File(route);
        Iterator<FileServiceOuterClass.ReadResponse> responses;
        OutputStream output;
        try {
            FileServiceOuterClass.ReadRequest request =
                    FileServiceOuterClass.ReadRequest.newBuilder()
                            .setName(name)
                            .setPos(0)
                            .setCount(bufferSize).build();
            long startTimestamp = System.currentTimeMillis();
            responses = stub.readServerStreaming(request);

            FileServiceOuterClass.ReadResponse response = null;

            if(responses.hasNext())
                response= responses.next();
            if (response.getCount() == -1) {
                System.out.println(
                        clientId +" -> No se encontro el archivo '" + name + "' en el servidor."
                );
                return;
            }

            file.delete();

            // true para que no se pisen los datos
            output = new FileOutputStream(file, true);

            if (file.createNewFile()) System.out.println(
                    clientId +" -> Se creo el archivo " + name
            );

            output.write(response.getData().toByteArray(), 0, response.getCount());

            while(responses.hasNext()){
                response = responses.next();

                if(response.getCount() > -1) {
                    output.write(response.getData().toByteArray(), 0, response.getCount());
                }
            }
            long endTimestamp = System.currentTimeMillis();
            System.out.println(clientId +" -> Tiempo: "+(endTimestamp - startTimestamp)+"ms");
            System.out.println(clientId +" -> Transferencia completada.");
            output.close();
        } catch (Exception e) {
            System.out.println(clientId +" -> Error al escribir el archivo.");
            e.printStackTrace();
        }
    }
}
