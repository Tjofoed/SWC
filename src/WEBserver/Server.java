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
            String path = "C:\\Users\\mikke_000\\iCloudDrive\\3 semester\\ChatTest\\src\\WEBserver\\";
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
                outToClient.write(("Date:" + new Date() + "\r\n").getBytes());
                outToClient.write("Server: Mikkels server\r\n".getBytes());


                if (fileName.endsWith(".jpg")) {
                    outToClient.write("Content-Type:image/jpeg\r\n".getBytes());
                }

                if (fileName.endsWith(".jpeg")) {
                    outToClient.write("Content-Type:image/jpeg\r\n".getBytes());
                }

                if (fileName.endsWith(".svg")) {
                    outToClient.write("Content-Type:vector/svg\r\n".getBytes());
                }

                if (fileName.endsWith(".html")) {
                    outToClient.write("Content-Type:text/html\r\n".getBytes());
                }

                if (fileName.endsWith(".gif")) {
                    outToClient.write("Content-Type:image/gif\r\n".getBytes());
                }

                if (fileName.endsWith(".txt")) {
                    outToClient.write("Content-Type:text/txt\r\n".getBytes());
                }

                if (fileName.endsWith(".mov")) {
                    outToClient.write("Content-Type:video/mov\r\n".getBytes());
                }

                if (fileName.endsWith(".rar")) {
                    outToClient.write("Content-Type:archive/rar\r\n".getBytes());
                }

                if (fileName.endsWith(".zip")) {
                    outToClient.write("Content-Type:archive/zip\r\n".getBytes());
                }

                if (fileName.endsWith(".png")) {
                    outToClient.write("Content-Type:image/png\r\n".getBytes());
                }

                if (fileName.endsWith(".doc")) {
                    outToClient.write("Content-Type:text/doc\r\n".getBytes());
                }

                if (fileName.endsWith(".mp4")) {
                    outToClient.write("Content-Type:video/mp4\r\n".getBytes());
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
    

