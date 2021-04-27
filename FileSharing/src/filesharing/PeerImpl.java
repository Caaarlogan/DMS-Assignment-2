package filesharing;

/**
 * @author Carlo Carbonilla
 */
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PeerImpl implements Peer {

    private HashMap<String, File> files;
    private String username;
    private ArrayList<String> connectedPeers;

    public PeerImpl(String username) {
        files = new HashMap();
        connectedPeers = new ArrayList<>();
        this.username = username;
    }

    @Override
    public String[] connectTo(String username) throws RemoteException {
        connectedPeers.add(username);

        return connectedPeers.toArray(new String[0]);
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
