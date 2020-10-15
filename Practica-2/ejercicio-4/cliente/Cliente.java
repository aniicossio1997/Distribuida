
/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

public class Cliente {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Cliente -> Debe indicar un identificador del cliente.");
                System.exit(1);
            }
            String identificador = args[0];
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
            remote.metodoRemoto(identificador);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}