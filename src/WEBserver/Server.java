package WEBserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{

    public static void main(String[] args)
    {
        System.out.println("OK, we are starting the WebServer.");

        try
        {
            ServerSocket listnerSocket = new ServerSocket(8080);
            System.out.println("OK, we have a listening socket.");

            while(true)
            {
                Socket newsocket = listnerSocket.accept();
                System.out.println("OK, we got a client connection!");
                ServiceTheClient(newsocket);
            }

        }
        catch(IOException e)
        {
            System.out.println("Webserver IO exception");
        }

    }


    public static void ServiceTheClient(Socket con)
    {
        Socket socket;
        socket = con;

        try
        {
            System.out.println("****************************************************************************");
            System.out.println("OK, we are starting to service the client.");
            String path = "C:\\Users\\Kofoed\\iCloudDrive\\3 semester\\ChatTest\\src\\WEBserver\\";
            String requestMessageLine;
            String fileName;

            //BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner inFromClient = new Scanner(socket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            requestMessageLine = inFromClient.nextLine();
            System.out.println("From Client:   " + requestMessageLine);

            StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

            if(tokenizedLine.nextToken().equals("GET"))
            {
                fileName = tokenizedLine.nextToken();

                if(fileName.startsWith("/") == true)
                {
                    fileName = path + fileName;
                }

                if(fileName.endsWith("/") == true)
                {
                    fileName = fileName + "index.html";
                }

                File file = new File(fileName);
                if (!file.isFile())
                {
                    fileName = path + "error404.html";
                    file = new File(fileName);
                }

                System.out.println("Trying to find file: " + fileName);

                int numOfBytes = (int)file.length();
                FileInputStream inFile = new FileInputStream(fileName);
                byte[] fileInBytes = new byte[numOfBytes];
                inFile.read(fileInBytes);
                inFile.close();  //***** remember to close the file after usage *****
                outToClient.writeBytes("HTTP/1.0 200 Her kommer skidtet\r\n");

                if(fileName.endsWith(".jpg"))
                {
                    outToClient.writeBytes("Content-Type:image/jpeg\r\n");
                }

                if(fileName.endsWith(".gif"))
                {
                    outToClient.writeBytes("Content-Type:image/gif\r\n");
                }

                outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
                outToClient.writeBytes("\r\n");
                outToClient.write(fileInBytes, 0, numOfBytes);
                outToClient.writeBytes("\n");

                System.out.println("OK, the file is sent to Client.");
                System.out.println("****************************************************************************");

                socket.close();
            }
            else // no "GET"
            {
                System.out.println("Bad request Message");
                outToClient.writeBytes("HTTP/1.0 400  I do not understand. I am from Barcelona.\r\n");
                outToClient.writeBytes("\n");
                socket.close();
            }
        }

        catch(IOException e)
        {
            System.out.println("IO Exception");
        }

    }  // end of 


}
    

