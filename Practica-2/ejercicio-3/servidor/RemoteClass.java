
/*
* RemoteClass.java
* Just implements the RemoteMethod interface as an extension to
* UnicastRemoteObject
*
*/
/* Needed for implementing remote method/s */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/* This class implements the interface with remote methods */
public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int bufferSize = 2048;

    protected RemoteClass() throws RemoteException {
        super();
    }

    public IResponse leer(String nombre, int posicion, int cantidad) throws RemoteException {
        String ruta = "/pdytr/Practica-2/ejercicio-3/servidor/archivos/" + nombre;
        File file = new File(ruta);
        IResponse respuesta = new Response();

        if (!file.exists()){
            respuesta.setCantidad(-1);
            return respuesta;
        }
        try {
            InputStream input = new FileInputStream(file);
            int leido = 0; // cantidad leida en un read
            byte[] buffer = new byte[bufferSize];

            input.skip(posicion);
         
            leido = input.read(buffer, 0, cantidad);
            
            respuesta.setCantidad(leido);
            respuesta.setDatos(buffer);                  

            input.close();
        } catch (Exception e) {
            System.out.println("Servidor -> Se produjo un error.");
            e.printStackTrace();
        }
        return respuesta;
    };

    public int escribir(String nombre, int cantidad, byte[] buffer) throws RemoteException {
        String ruta = "/pdytr/Practica-2/ejercicio-3/servidor/archivos/" + nombre;
        File file = new File(ruta);
        try {
            if (file.createNewFile())
                System.out.println("Servidor -> Se creo el archivo " + nombre);
            OutputStream output = new FileOutputStream(file, true);// true para que no se pisen los datos
            output.write(buffer, 0, cantidad);
            output.close();
        } catch (Exception e) {
            System.out.println("Servidor -> Error al escribir el archivo.");
            e.printStackTrace();
        }
        return cantidad;
    };

}