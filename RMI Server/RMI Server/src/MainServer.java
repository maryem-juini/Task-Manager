import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        System.out.println("Demarre serveur");
        String url = "rmi://127.0.0.1:9001/chat";
        ChatImplementataion chat = null;
        try {
            chat = new ChatImplementataion();
            LocateRegistry.createRegistry(9001);
            Naming.rebind(url, chat);
            System.out.println("Serveur en ecoute...");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}
