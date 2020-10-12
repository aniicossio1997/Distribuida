
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
import java.util.Scanner;

public class Cliente {

    private static int bufferSize = 2048;

    private static void escribir(IfaceRemoteClass remote, String nombre) {
        final String ruta = "/pdytr/Practica-2/ejercicio-3/cliente/archivos/" + nombre;
        try {
            File archivo = new File(ruta);
            InputStream input = new FileInputStream(archivo);
            int leido = 0; // cantidad leida en un read
            byte[] buffer = new byte[bufferSize];
            Long total = archivo.length();
            while (leido > -1) {                      
                leido = input.read(buffer, 0, bufferSize);          
                if (leido > 0) { // si no hay error ni se termino el archivo
                    remote.escribir(nombre, leido, buffer);
                }
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        System.out.println("Transferencia completada exitosamente.");    
    }

    public static void main(String[] args) {  
        try {
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Ingrese el nombre del archivo: ");      
            escribir(remote, keyboard.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}