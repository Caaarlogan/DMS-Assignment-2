package filesharing;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Carlo Carbonilla
 */

public class RunRegistry {
    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(1099);
        System.out.println("Running RMI Registry");
        while(true);
    }
}