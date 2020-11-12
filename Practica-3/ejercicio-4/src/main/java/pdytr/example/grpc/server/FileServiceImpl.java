package pdytr.example.grpc.server;

import com.google.protobuf.ByteString;
import io.grpc.stub.CallStreamObserver;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import pdytr.example.grpc.FileServiceGrpc;
import pdytr.example.grpc.FileServiceOuterClass;

public class FileServiceImpl extends FileServiceGrpc.FileServiceImplBase {

  private static String directorioArchivos =
          "files/server/";

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
          FileServiceOuterClass.ReadResponse.newBuilder().setCount(readCount).setData(ByteString.copyFrom(buffer))
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
      if (file.createNewFile()) System.out.println(
        "Servidor -->Se creo el archivo " + request.getName()
      );

      OutputStream output = new FileOutputStream(file, true); // true para que no se pisen los datos
      output.write(request.getData().toByteArray(), 0, request.getCount());
      output.close();
      response =
        FileServiceOuterClass.WriteResponse
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

  @Override
  public void readServerStreaming(
          FileServiceOuterClass.ReadRequest request,
          StreamObserver<FileServiceOuterClass.ReadResponse> streamObserver
  ) {

    FileServiceOuterClass.ReadResponse response = null;

    String ruta = directorioArchivos + request.getName();
    System.out.println("Servidor --> se solicita el archivo " + request.getName());
    File file = new File(ruta);

    if (!file.exists()) {
      System.out.println("Servidor --> El archivo solicitado no existe.");
      streamObserver.onNext(FileServiceOuterClass.ReadResponse.newBuilder().setCount(-1).build());
    } else {
      try {
        InputStream input = new FileInputStream(file);
        int readCount = 0; // cantidad leida en un read
        byte[] buffer = new byte[bufferSize];

        while (readCount > -1) {
          readCount = input.read(buffer, 0, bufferSize);
          response =
                  FileServiceOuterClass.ReadResponse.newBuilder()
                          .setData(ByteString.copyFrom(buffer))
                          .setCount(readCount).build();
          streamObserver.onNext(response);
        }
        input.close();
      } catch (Exception e) {
        System.out.println("Servidor -> Se produjo un error.");
        e.printStackTrace();
      }
    }
    System.out.println("Servidor --> Transferencia completada");
    streamObserver.onCompleted();
  }
}
