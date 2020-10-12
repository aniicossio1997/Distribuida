
/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

public class Cliente {

    private static int bufferSize = 1000;

    private static void escribir(IfaceRemoteClass remote, String nombre) {
        final String ruta = "/pdytr/Practica-2/ejercicio-3/cliente/archivos/" + nombre;
        try {

            File archivo = new File(ruta);
            InputStream input = new FileInputStream(archivo);
            int i = 0; // cantidad leida hasta el momento
            byte[] buffer = new byte[bufferSize];
            while (i != -1) {
                i = input.read(buffer, i, bufferSize);
                if (i != -1) {
                    remote.escribir(nombre, i, buffer);
                }
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error");
        }
    }

    public static void main(String[] args) {
        /* Look for hostname and msg length in the command line */
        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
            escribir(remote, "logo.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}