package pdytr.example.grpc;
import io.grpc.*;
import pdytr.example.grpc.server.FileServiceImpl;

public class App
{
    public static void main( String[] args ) throws Exception
    {
      // Create a new server to listen on port 8080
      Server server = ServerBuilder.forPort(8080)
        .addService(new FileServiceImpl())
        .build();
      // Start the server
      server.start();
      // Server threads are running in the background.
      System.out.println("Server started");
      // Don't exit the main thread. Wait until server is terminated.
      server.awaitTermination();
    }
}