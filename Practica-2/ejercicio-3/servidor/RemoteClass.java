
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

    /* Remote method implementation */
    public byte[] sendThisBack(byte[] data) throws RemoteException {
        System.out.println("Data back to client");
        return data;
    }

    public String leer(String nombre, int posicion, int cantidad) throws RemoteException {
        System.out.println("Nombre: " + nombre + " Posicion: " + posicion + " Cantidad: " + cantidad);
        return "asdasdads";
    };

    public int escribir(String nombre, int cantidad, byte[] buffer) throws RemoteException {
        String ruta = "/pdytr/Practica-2/ejercicio-3/servidor/archivos/" + nombre;
        File file = new File(ruta);
    
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream output = new FileOutputStream(file);

            output.write(buffer, (int) file.length(), cantidad);
        } catch (Exception e) {
            System.out.println("Error al escribir el archivo.");
        }
        return cantidad;
    };

}