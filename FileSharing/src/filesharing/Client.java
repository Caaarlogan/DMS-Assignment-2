/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Oscar
 */
public class Client {

    PeerImpl peer;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        InetAddress ip;

        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Enter your username");
            String username = scan.nextLine();

            PeerImpl remoteObject = new PeerImpl(username + "@" + ip);
            try {  // create stub (note prior to Java 5.0 must use rmic utility)
                Peer stub = (Peer) UnicastRemoteObject.exportObject(remoteObject, 0);

                // create the registry which is running on the default port 1099
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(username, stub);//binds if not already
                // display the names currently bound in the registry
                System.out.println("Names bound in RMI registry");
                try {
                    String[] bindings = Naming.list("localhost"); // no URL
                    for (String name : bindings) {
                        System.out.println(name);
                    }
                }
                catch (MalformedURLException e) {
                    System.err.println("Unable to see names: " + e);
                }
                System.out.println("what do u want to do (1 for select file)");
            }
            catch (RemoteException e) {
                System.err.println("Unable to bind to registry: " + e);
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String selectFile() throws RemoteException {
        Set<String> files = peer.getAvailableFiles();
        String out = "Available files:\n";
        for (String s : files) {
            out += s + "\n";
        }
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

    }
}
