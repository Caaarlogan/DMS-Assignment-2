package filesharing;

/**
 * @author Carlo Carbonilla
 */

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Peer extends Remote{
    public void connectTo(String username) throws RemoteException;
    public boolean addUser(String username) throws RemoteException;
    public void leaveDS() throws RemoteException;
    public boolean removeUser(String username) throws RemoteException;
    public Set<String> getUsers() throws RemoteException;
    public File getFile(String fileName) throws RemoteException;
    public Set<String> getAvailableFiles() throws RemoteException;
}
