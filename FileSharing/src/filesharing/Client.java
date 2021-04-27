/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Oscar
 */
public class Client {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        InetAddress ip;

        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Enter your username");
            String username = scan.nextLine() + "@" + ip;
            
            System.out.println(username);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
    public void selectFile(){
        
    }
}
