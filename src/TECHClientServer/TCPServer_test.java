package TECHClientServer;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPServer_test
{
    public static void main(String args[]) throws Exception
    {
        System.out.println("starting TCP Server main program");
        String sentence;
        String userText;

        ServerSocket welcomeSocket = new ServerSocket(5656);
        System.out.println("we have a socket");
        Scanner inFromUser = new Scanner(System.in);

        System.out.println("waiting for a connection");
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        boolean sentinel = false;

        while(!sentinel) {
            sentence = inFromClient.readLine();
            System.out.println("FROM CLIENT: " + sentence);

            if(sentence.equalsIgnoreCase("quit")){
                sentinel = true;
                System.out.println("QUITTING");
                connectionSocket.close();
                welcomeSocket.close();
            }else {
                System.out.print("Please type your text: ");
                userText = inFromUser.nextLine();
                outToClient.writeBytes(userText + '\n');

                if (sentence.equalsIgnoreCase("quit")) {
                    sentinel = true;
                    System.out.println("QUITTING2");
                    connectionSocket.close();
                    welcomeSocket.close();
                }
            }
        }
    }

}