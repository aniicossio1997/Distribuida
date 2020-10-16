
/*
* IfaceRemoteClass.java
* Interface defining only one method which can be invoked remotely
*
*/
/* Needed for defining remote method/s */
import java.rmi.Remote;
import java.rmi.RemoteException;

/* This interface will need an implementing class */
public interface IfaceRemoteClass extends Remote {

    public IResponse leer(String nombre, int posicion, int cantidad) throws RemoteException;
    
    public int escribir( String nombre, int cantidad, byte[] buffer ) throws RemoteException;

    public boolean crearArchivo(String nombre) throws RemoteException;
}