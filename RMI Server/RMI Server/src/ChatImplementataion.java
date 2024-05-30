import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatImplementataion extends UnicastRemoteObject implements ChatREMOTE {
    ArrayList<Message>disc=new ArrayList<>();

    protected ChatImplementataion() throws RemoteException {
    }

    protected ChatImplementataion(int port) throws RemoteException {
        super(port);
    }

    protected ChatImplementataion(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public ArrayList getMessage() throws RemoteException {
        return disc;
    }

    @Override
    public void AddMsg(Message m) throws RemoteException {
        this.disc.add(m);

    }
}
