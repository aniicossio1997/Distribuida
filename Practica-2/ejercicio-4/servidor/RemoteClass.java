
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

public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass {

    private static final long serialVersionUID = 1L;

    protected RemoteClass() throws RemoteException {
        super();
    }

    public void metodoRemoto(String identificador) throws RemoteException {
        while (true) {
            System.out.println("Servidor -> Atendiendo a : " + identificador);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("Servidor -> Error de interrupcion - " + identificador);
            }         
        }

    };

}