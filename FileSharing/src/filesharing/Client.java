/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar
 */
public class Client {

    private final String fileFolderPath = System.getProperty("user.home") + "/Desktop/Assignment2";
    private final File fileFolder;
    
    private PeerImpl peer;
    private InetAddress ip;
    private Set<String> registryUsers;
    private Set<String> dsUsers;
    private Registry registry;
    private String username;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    
    public Client(){
        fileFolder = new File(fileFolderPath);
    }

    public void start() {

        try {
            Scanner scan = new Scanner(System.in);
            ip = InetAddress.getLocalHost();
            registryUsers = new HashSet();

            try {

                // create the registry which is running on the default port 1099
                registry = LocateRegistry.getRegistry();

                System.out.println("Users in the RMI Registry");
                // get all usernames currently bound in the registry
                try {
                    String[] bindings = Naming.list("localhost"); // no URL
                    for (String name : bindings) {
                        registryUsers.add(name);
                        System.out.println(name);
                    }

                    System.out.println("\nEnter your username");
                    username = scan.nextLine();

                    while (registryUsers.contains("//:1099/" + username)) {
                        System.out.println("Username already taken, please enter a new one");
                        username = scan.nextLine();
                    }

                    username = username + "@" + ip;
                    peer = new PeerImpl(username);
                    int filesAdded = peer.addFiles(fileFolder);
                    System.out.println("loaded " + filesAdded + " files");

                    // create stub
                    Peer stub = (Peer) UnicastRemoteObject.exportObject(peer, 0);
                    
                    registry.rebind(username, stub);//binds if not already

                    options(scan, stub);

                }
                catch (MalformedURLException e) {
                    System.err.println("Unable to see names: " + e);
                }
            }
            catch (RemoteException e) {
                System.err.println("Unable to bind to registry: " + e);
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void options(Scanner scan, Peer stub) {
        System.out.println("What would you like to do?");
        System.out.println("1) Join distributed system");
        System.out.println("2) List users in distributed system");
        System.out.println("3) Leave distributed system");

        String option = scan.nextLine();

        if (option.equals("1")) {
            joinDS(scan, stub);
            options(scan, stub);
        }
        if (option.equals("2")) {
            printDSUsers(stub);
            options(scan, stub);
        }
        else {
            leaveDS(stub);
            options(scan, stub);
        }
    }

    public void printDSUsers(Peer stub) {
        try {
            dsUsers = stub.getUsers();
            System.out.println("Users in the distributed system");

            if (dsUsers.isEmpty()) {
                System.out.println("No users");
            }
            else {
                for (String user : dsUsers) {
                    System.out.println(user);
                }
            }

        }
        catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void joinDS(Scanner scan, Peer stub) {
        try {
            dsUsers = stub.getUsers();

            if (dsUsers.isEmpty()) {
                System.out.println("Enter a user to join their peer to peer distributed system");
                String joinUser = scan.nextLine();

                while (!registryUsers.contains("//:1099/" + joinUser)) {
                    System.out.println("Please enter a user in the RMI Registry");
                    joinUser = scan.nextLine();
                }

                stub.connectTo(joinUser);
                dsUsers = stub.getUsers();
            }
            else {
                System.out.println("Already part of a distributed system");
            }
        }
        catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leaveDS(Peer stub) {
        try {
            dsUsers = stub.getUsers();

            if (dsUsers.isEmpty()) {
                System.out.println("Not part of a distributed system");
            }
            else {
                stub.leaveDS();
                dsUsers.clear();
            }
        }
        catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Removes user from registry and ends program
    public void terminate() {
        try {
            //unbind user to registry
            registry.unbind(username);

            //remove stub
            UnicastRemoteObject.unexportObject(peer, true);
        }
        catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String selectFile() throws RemoteException {
        Set<String> files = peer.getAvailableFiles();
        String out = "Available files:\n";
        for (String s : files) {
            out += s + "\n";
        }
        System.out.println(out);
        System.out.println("Enter file to get: ");
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.next();
        if (files.contains(input)) {
            return input;
        }
        else {
            System.out.println("file not found");
        }
        return null;
    }

    public void printUsers(String[] users) {
        String out = "Connected to users:\n";
        for (String s : users) {
            out += s + "\n";
        }
        System.out.println(out);

    }

    private void saveFile(File f) {
        String name = f.getName();
        File file = new File(fileFolderPath + "/" + name);
        
        try {
            OutputStream out = new FileOutputStream(file);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
