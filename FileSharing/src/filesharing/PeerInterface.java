package filesharing;

/**
 * @author Carlo Carbonilla
 */

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PeerInterface extends Remote{
    public String[] connectTo(String username) throws RemoteException;
    public File getFile(String fileName) throws RemoteException;
    public String[] getAvailableFiles() throws RemoteException;
}
