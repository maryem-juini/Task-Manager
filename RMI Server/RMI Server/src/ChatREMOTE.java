import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatREMOTE extends Remote {
    public ArrayList getMessage() throws RemoteException;
    public void AddMsg(Message m) throws RemoteException;
}
