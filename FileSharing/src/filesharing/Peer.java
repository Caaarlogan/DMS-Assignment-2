package filesharing;

/**
 * @author Carlo Carbonilla
 */
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Peer implements PeerInterface {

    //actually maybe use hashmap with string filename>actual file object, so that can easily say what file i have and then share it if wanted.
    HashMap<String, File> files;
    String username;
    ArrayList<String> connectedPeers;

    public Peer(String username) {
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
    public String[] getAvailableFiles() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        InetAddress ip;

        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Enter your username");
            String username = scan.nextLine();

            System.out.println(username + "@" + ip);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
