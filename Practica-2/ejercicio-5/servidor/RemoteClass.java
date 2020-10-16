
/*
* RemoteClass.java
* Just implements the RemoteMethod interface as an extension to
* UnicastRemoteObject
*
*/
/* Needed for implementing remote method/s */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass {

    private static final long serialVersionUID = 1L;

    protected RemoteClass() throws RemoteException {
        super();
    }

    public void metodoInutil() throws RemoteException {
       
    };

}