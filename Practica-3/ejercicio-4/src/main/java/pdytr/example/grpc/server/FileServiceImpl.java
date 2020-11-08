package pdytr.example.grpc.server;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.FileServiceGrpc;
import pdytr.example.grpc.FileServiceOuterClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileServiceImpl extends FileServiceGrpc.FileServiceImplBase {

    private static String directorioArchivos =
            "/pdytr/Practica-3/ejercicio-4/src/main/java/pdytr/example/grpc/server/files/";

    private static int bufferSize = 32768;

    @Override
    public void read(
            FileServiceOuterClass.ReadRequest request,
            StreamObserver<FileServiceOuterClass.ReadResponse> responseObserver
    ) {
    
        FileServiceOuterClass.ReadResponse response = null;

        String ruta = directorioArchivos + request.getName();

        File file = new File(ruta);

        if (!file.exists()) {
            System.out.println("Servidor --> El archivo solicitado no existe.");
            response =
                    FileServiceOuterClass.ReadResponse.newBuilder().setCount(-1).build();
        } else {
            try {
                InputStream input = new FileInputStream(file);

                int readCount; // cantidad leida en un read
                byte[] buffer = new byte[bufferSize];

                input.skip(request.getPos());

                readCount = input.read(buffer, 0, request.getCount());

                response =
                        FileServiceOuterClass.ReadResponse
                                .newBuilder()
                                .setCount(readCount)
                                .setData(ByteString.copyFrom(buffer))
                                .build();

                input.close();
            } catch (Exception e) {
                System.out.println("Servidor -> Se produjo un error.");
                e.printStackTrace();
            }
        }

        responseObserver.onNext(response);

        responseObserver.onCompleted();
        //System.out.println("===========================================================");
    }

    @Override
    public void write(
            FileServiceOuterClass.WriteRequest request,
            StreamObserver<FileServiceOuterClass.WriteResponse> responseObserver
    ) {
        //System.out.println("Write Request: ");

        FileServiceOuterClass.WriteResponse response = null;

        String ruta = directorioArchivos + request.getName();

        File file = new File(ruta);

        try {
            if (file.createNewFile())
                System.out.println("Servidor -->Se creo el archivo " + request.getName());

            OutputStream output = new FileOutputStream(file, true);// true para que no se pisen los datos
            output.write(request.getData().toByteArray(), 0, request.getCount());
            output.close();
            response = FileServiceOuterClass.WriteResponse
                    .newBuilder()
                    .setCount(request.getCount())
                    .build();

            responseObserver.onNext(response);

        } catch (Exception e) {
            System.out.println("Servidor -> Se produjo un error.");
            e.printStackTrace();
        }

        responseObserver.onCompleted();
        //System.out.println("===========================================================");
    }
}
