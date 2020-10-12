
/*
* RemoteClass.java
* Just implements the RemoteMethod interface as an extension to
* UnicastRemoteObject
*
*/
/* Needed for implementing remote method/s */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/* This class implements the interface with remote methods */
public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected RemoteClass() throws RemoteException {
        super();
    }

    public String leer(String nombre, int posicion, int cantidad) throws RemoteException {
        System.out.println("Nombre: " + nombre + " Posicion: " + posicion + " Cantidad: " + cantidad);
        return "asdasdads";
    };

    public int escribir(String nombre, int cantidad, byte[] buffer) throws RemoteException {
        String ruta = "/pdytr/Practica-2/ejercicio-3/servidor/archivos/" + nombre;
        File file = new File(ruta);
        try {
            if (file.createNewFile())
                System.out.println("Se creo el archivo " + nombre);          
            OutputStream output = new FileOutputStream(file, true);//true para que no se pisen los datos
            output.write(buffer, 0, cantidad);
        } catch (Exception e) {
            System.out.println("Error al escribir el archivo.");
            e.printStackTrace();
        }
        return cantidad;
    };

}