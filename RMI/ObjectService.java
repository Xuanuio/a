package RMI;
import java.rmi.*;
import java.io.Serializable;

public interface ObjectService extends Remote {
    public Serializable requestObject(String studentCode, String qAlias) throws RemoteException;
    public void submitObject(String studentCode, String qAlias, Serializable object) throws RemoteException;
}
