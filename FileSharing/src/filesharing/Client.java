/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Oscar
 */
public class Client {
    Peer peer;
    
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    
    public void start(){
        Scanner scan = new Scanner(System.in);
        
        InetAddress ip;
        String username = null;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Enter your username");
            username = scan.nextLine() + "@" + ip;
            
            System.out.println(username);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        peer = new Peer(username);
        System.out.println("what do u want to do (1 for select file)");
        
        
    }
    
    public String selectFile(){
        Set<String> files = peer.getAvailableFiles();
        String out = "Available files:\n";
        for (String s : files){
            out += s + "\n";
        }
        System.out.println("Enter file to get: ");
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.next();
        if (files.contains(input)){
            return input;
        }else{
            System.out.println("file not found");
        }
        return null;
    }
    
    public void printUsers(String[] users){
        String out = "Connected to users:\n";
        for(String s : users){
            out += s + "\n";
        }
        System.out.println(out);
        
    }
    
    private void saveFile(File f){
        
    }
}
