import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// Interface declaration
public interface RemoteListService extends Remote {
    String registerUser(String username, String password) throws RemoteException;
    String signInUser(String username, String password) throws RemoteException;
    void updateUserList(String username, String action, String itemName, String itemDeadline) throws RemoteException;
    List<String> displayUserList(String username) throws RemoteException;
    String downloadSortedUserList(String username) throws RemoteException;
}
