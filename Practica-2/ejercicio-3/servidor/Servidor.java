
/*
* StartRemoteObject.java
* Starts the remote object. More specifically:
* 1) Creates the object which has the remote methods to be invoked
* 2) Registers the object so that it becomes avaliable
*/
import java.rmi.registry.Registry; /* REGISTRY_PORT */
import java.rmi.Naming; /* rebind */

public class Servidor {

    public static void main(String args[]) {
        try {         
            RemoteClass robject = new RemoteClass();
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(rname, robject);
            System.out.println("Servidor -> escuchando...");
        } catch (Exception e) {
            System.out.println("Servidor -> Hey, an error occurred at Naming.rebind");
            e.printStackTrace();
        }

    }
}