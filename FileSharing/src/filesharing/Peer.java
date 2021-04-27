package filesharing;

/**
 * @author Carlo Carbonilla
 */

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Peer extends Remote{
    public String[] connectTo(String username) throws RemoteException;
    public File getFile(String fileName) throws RemoteException;
    public Set<String> getAvailableFiles() throws RemoteException;
}
