
/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */
import java.util.Scanner;

public class Cliente {

    private static int bufferSize = 2048;

    static String getCopyName (String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos)+"_copy"+str.substring(pos, str.length());
    }
    
    private static boolean escribir(IfaceRemoteClass remote, String name, boolean asCopy) {
        final String ruta = "/pdytr/Practica-2/ejercicio-3/cliente/archivos/" + name;
        String nameInServer = asCopy ? getCopyName(name) : name;
        try {
            File file = new File(ruta);
            if(!file.exists()){
                System.out.println("Cliente -> No se encontro el archivo.");
                return false;
            }
            InputStream inputStream = new FileInputStream(file);
            int read = 0; // cantidad leida en un read
            byte[] buffer = new byte[bufferSize];
            while (read > -1) {
                read = inputStream.read(buffer, 0, bufferSize);
                if (read > 0) { // si no hay error ni se termino el archivo
                    remote.escribir(nameInServer, read, buffer);
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

    private static boolean leer(IfaceRemoteClass remote, String name) {
        String route = "/pdytr/Practica-2/ejercicio-3/cliente/archivos/" + name;
        File file = new File(route);
        long offset = 0;
        IResponse response = new Response();
        response.setCantidad(0);
        OutputStream output = null;
        try {
            response = remote.leer(name, (int) offset, bufferSize);
            if (response.getCantidad() > -1) {
                if (file.createNewFile())
                    System.out.println("Cliente -> Se creo el archivo " + name);
                output = new FileOutputStream(file, true);// true para que no se pisen los datos
                output.write(response.getDatos(), 0, response.getCantidad());
                offset = offset + response.getCantidad();
            }else{
                System.out.println("Cliente -> No se encontro el archivo '" + name+ "' en el servidor.");
            }
            while (response.getCantidad() > -1) {
                response = remote.leer(name, (int) offset, bufferSize);           
                if (response.getCantidad() > -1) {
                    output.write(response.getDatos(), 0, response.getCantidad());
                    offset = offset + response.getCantidad();
                }
            }
            if(offset > 0){
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

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Cliente -> Debe indicar el nombre del archivo.");
                System.exit(1);
            }
            String fileName = args[0];
            System.out.print("Cliente -> Nombre del archivo: "+fileName);
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
            //Scanner keyboard = new Scanner(System.in);
            //System.out.println("Ingrese el nombre del archivo: ");
            //fileName= keyboard.next();
           // keyboard.close();
            boolean success = leer(remote, fileName);
            if(success){
             escribir(remote, fileName, true);
            }          
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}