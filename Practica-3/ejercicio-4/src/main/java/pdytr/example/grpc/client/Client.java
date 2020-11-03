package pdytr.example.grpc.client;

import com.google.protobuf.ByteString;
import io.grpc.*;
import pdytr.example.grpc.FileServiceGrpc;
import pdytr.example.grpc.FileServiceOuterClass;

import java.io.*;

public class Client {

    private static FileServiceGrpc.FileServiceBlockingStub stub;

    private static int bufferSize = 32768;

    private static String filesFolder =
            "/pdytr/Practica-3/ejercicio-4/src/main/java/pdytr/example/grpc/client/files/";

    public static void main(String[] args) throws Exception {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:8080")
                .usePlaintext(true)
                .build();


        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        stub = FileServiceGrpc.newBlockingStub(
                channel
        );

        FileServiceOuterClass.ReadRequest request = FileServiceOuterClass.ReadRequest
                .newBuilder()
                .setName("practica.pdf")
                .setPos(123)
                .setCount(1200)
                .build();

        // Finally, make the call using the stub
        //FileServiceOuterClass.ReadResponse response = stub.read(request);

        if (args.length != 2) {
            System.out.println("Cliente -> Debe indicar la operacion a realizar (read/write)");
            System.out.println("Cliente -> Debe indicar el nombre del archivo.");
            System.exit(1);
        }

        String operation = args[0];
        String fileName = args[1];

        System.out.println("Cliente -> Nombre del archivo: "+fileName);

        System.out.println("Cliente -> Operacion: "+operation);

        if(operation.equalsIgnoreCase("read"))
            read(fileName);
        else if (operation.equalsIgnoreCase("write"))
            write(fileName);


        // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();
    }

    private static boolean read(String name) {
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
            response = stub.read(request);
            if (response.getCount() > -1) {
                if (file.createNewFile()) System.out.println(
                        "Cliente -> Se creo el archivo " + name
                );
                output = new FileOutputStream(file, true); // true para que no se pisen los datos
                output.write(response.getData().toByteArray(), 0, response.getCount());
                offset = offset + response.getCount();
            } else {
                System.out.println(
                        "Cliente -> No se encontro el archivo '" + name + "' en el servidor."
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
            if (offset > 0) {
                System.out.println("Cliente -> Transferencia completada.");
                output.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Cliente -> Error al escribir el archivo.");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static boolean write(String name) {
        System.out.println("hola wtff");
        final String ruta = filesFolder + name;
        try {
            File file = new File(ruta);
            if(!file.exists()){
                System.out.println("Cliente -> No se encontro el archivo.");
                return false;
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
                    System.out.println("write");
                }
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Cliente -> Se produjo un error.");
            e.printStackTrace();
            return false;
        }
        System.out.println("Cliente -> Transferencia completada exitosamente.");
        return true;
    }
}
