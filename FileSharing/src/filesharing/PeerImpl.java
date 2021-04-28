package filesharing;

/**
 * @author Carlo Carbonilla
 */
import java.io.File;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerImpl implements Peer {

    private HashMap<String, File> files;
    private String username;
    private Set<String> connectedPeers;
    private Registry registry;

    public PeerImpl(String username) {
        files = new HashMap();
        connectedPeers = new HashSet();
        this.username = username;
    }

    @Override
    public void connectTo(String username) throws RemoteException {
        try {
            registry = LocateRegistry.getRegistry("localhost");

            addUser(username);

            //Access user you want to connect to and have them add you as a peer
            Peer remoteProxy = (Peer) registry.lookup(username);
            remoteProxy.addUser(this.username);

            //Access other users in distributed system and have them add you as a peer
            Set<String> users = remoteProxy.getUsers();

            for (String user : users) {
                if (!user.equals(this.username)) {
                    Peer rp = (Peer) registry.lookup(user);
                    rp.addUser(this.username);
                }
            }
        }
        catch (NotBoundException ex) {
            Logger.getLogger(PeerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (AccessException ex) {
            Logger.getLogger(PeerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean addUser(String username) throws RemoteException {
        return connectedPeers.add(username);
    }

    @Override
    public void leaveDS() throws RemoteException {
        try {
            registry = LocateRegistry.getRegistry("localhost");
            
            for (String user : connectedPeers) {
                if (!user.equals(this.username)) {
                    Peer rp = (Peer) registry.lookup(user);
                    rp.removeUser(this.username);
                }
            }
            
            connectedPeers.clear();
        }
        catch (NotBoundException ex) {
            Logger.getLogger(PeerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (AccessException ex) {
            Logger.getLogger(PeerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean removeUser(String username) throws RemoteException {
        return connectedPeers.remove(username);
    }

    @Override
    public Set<String> getUsers() throws RemoteException {
        return connectedPeers;
    }

    @Override
    public File getFile(String fileName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> getAvailableFiles() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
