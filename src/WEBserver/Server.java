package WEBserver;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server
{

    public static void main(String[] args)
    {
        System.out.println("****STARTING SERVER****");

        try
        {
            ServerSocket listenerSocket = new ServerSocket(8080);
            System.out.println("****SERVER READY*****");

            while(true)
            {
                Socket socket = listenerSocket.accept();
                Thread serviceTheClient = new Thread(() -> {
                    System.out.println("****CONNECTION ESTABLISHED****");
                    ServiceTheClient(socket);
                });
                serviceTheClient.start();
            }

        }
        catch(IOException e)
        {
            System.out.println("****CONNECTION COULD NOT BE ESTABLISHED****");
        }

    }


    public static void ServiceTheClient(Socket con)
    {
        Socket socket;
        socket = con;

        try
        {
            System.out.println("****SERVICE STARTED****");
            String path = "C:\\Users\\Kofoed\\iCloudDrive\\3 semester\\ChatTest\\src\\WEBserver\\";
            String requestMessageLine;
            String fileName;

            Scanner inFromClient = new Scanner(socket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            requestMessageLine = inFromClient.nextLine();
            System.out.println("REQUEST FROM CLIENT: " + requestMessageLine);

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
                    System.out.println("****FILE NOT FOUND IN SYSTEM****");
                }

                System.out.println("LOCATING FILE: " + fileName);

                int numOfBytes = (int)file.length();
                FileInputStream inFile = new FileInputStream(fileName);
                byte[] fileInBytes = new byte[numOfBytes];
                inFile.read(fileInBytes);
                inFile.close();  //***** remember to close the file after usage *****
                outToClient.writeBytes("HTTP/1.0 200 OK\r\n");
                outToClient.writeBytes("Date:" + new Date() + "\r\n");
                outToClient.writeBytes("Server: Mikkels server\r\n");


                if (fileName.endsWith(".jpg")) {
                    outToClient.writeBytes("Content-Type:image/jpeg\r\n");
                }

                if (fileName.endsWith(".jpeg")) {
                    outToClient.writeBytes("Content-Type:image/jpeg\r\n");
                }

                if (fileName.endsWith(".svg")) {
                    outToClient.writeBytes("Content-Type:vector/svg\r\n");
                }

                if (fileName.endsWith(".html")) {
                    outToClient.writeBytes("Content-Type:text/html\r\n");
                }

                if (fileName.endsWith(".gif")) {
                    outToClient.writeBytes("Content-Type:image/gif\r\n");
                }

                if (fileName.endsWith(".txt")) {
                    outToClient.writeBytes("Content-Type:text/txt\r\n");
                }

                if (fileName.endsWith(".mov")) {
                    outToClient.writeBytes("Content-Type:video/mov\r\n");
                }

                if (fileName.endsWith(".rar")) {
                    outToClient.writeBytes("Content-Type:archive/rar\r\n");
                }

                if (fileName.endsWith(".zip")) {
                    outToClient.writeBytes("Content-Type:archive/zip\r\n");
                }

                if (fileName.endsWith(".png")) {
                    outToClient.writeBytes("Content-Type:image/png\r\n");
                }

                if (fileName.endsWith(".doc")) {
                    outToClient.writeBytes("Content-Type:text/doc\r\n");
                }

                if (fileName.endsWith(".mp4")) {
                    outToClient.writeBytes("Content-Type:video/mp4\r\n");
                }

                outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
                outToClient.writeBytes("\r\n");
                outToClient.write(fileInBytes, 0, numOfBytes);
                outToClient.writeBytes("\n");

                System.out.println("****FILE SENT TO CLIENT****");

                socket.close();
            }
            else // no "GET"
            {
                System.out.println("BAD REQUEST");
                outToClient.writeBytes("HTTP/1.0 500 BAD REQUEST\r\n");
                outToClient.writeBytes("\n");
                socket.close();
            }
        }

        catch(IOException e)
        {
            System.out.println("IO Exception");
        }
        catch(NoSuchElementException e){
            System.out.println("No such element Exception");
        }

    }  // end of 


}
    

