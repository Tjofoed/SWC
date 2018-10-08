package TECHClientServer;
/**
 * Write a description of class UDP_1 here.
 *
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDP_11t
{
    public static void main(String args[]) throws Exception
    {
        String sentence;
        int length;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Scanner inFromKbd = new Scanner(System.in);
        DatagramSocket receivingSocket = new DatagramSocket(6701);
        DatagramSocket sendingSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
        byte[] data = new byte[1024];
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            receivingSocket.receive(receivePacket);
            sentence = new String(receivePacket.getData());
            if(sentence.substring(0,4).equalsIgnoreCase("quit")) {
                System.out.println("QUITTING");
                System.exit(1);
            }else {
                System.out.println("FROM SERVER:" + sentence);
            }

            System.out.println("Please type you message: ");
            sentence = inFromUser.readLine();
            length = sentence.length();
            data = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, length, IPAddress, 6710);
            sendingSocket.send(sendPacket);
            if(sentence.length() == 4 && sentence.substring(0,4).equalsIgnoreCase("quit")) {
                System.out.println("QUITTING");
                System.exit(1);
            }
            sentence = "                               ";
            data = sentence.getBytes();
        }

    }
}


