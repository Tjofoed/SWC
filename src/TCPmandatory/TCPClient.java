package TCPmandatory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class TCPClient {

    public static void main(String[] args) {

        System.out.println("=============CLIENT==============");
        String username;
        Scanner sc = new Scanner(System.in);
      //  final int PORT_SERVER = 5757; //INTERN
        final int PORT_SERVER = 5656; //EXTERN
       // final String IP_SERVER_STR = "127.0.0.1"; //INTERN
       final String IP_SERVER_STR = "172.16.19.196"; //EXTERN


        try {
            InetAddress ip = InetAddress.getByName(IP_SERVER_STR);

            System.out.println("\nConnecting...");
            System.out.println("SERVER IP: " + IP_SERVER_STR);
            System.out.println("SERVER PORT: " + PORT_SERVER);

            Socket socket = new Socket(ip, PORT_SERVER);
            System.out.println("Connection established.\n");

            System.out.println("----JOIN----");
            System.out.println("Username must be a maximum of 12 characters and may only contain letters, digits, '-' and '_'.");
            boolean joinCheck = true;


            do {
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                do {
                    System.out.print("Enter username: ");
                    username = sc.next();
                }
                while(!checkUsername(username));
                String msgToSend = "JOIN " + username + ", " + IP_SERVER_STR + ":" + PORT_SERVER;
                byte[] dataToSend = msgToSend.getBytes();
                output.write(dataToSend);

                byte[] dataIn = new byte[1024];
                input.read(dataIn);
                String msgIn = new String(dataIn);
                msgIn = msgIn.trim();

                if (msgIn.substring(0, 4).equalsIgnoreCase("J_OK")) {
                    System.out.println("****USERNAME OK****\n");
                    joinCheck = false;
                } else if(msgIn.substring(0, 4).equalsIgnoreCase("J_ER")){
                    System.out.println("****" + msgIn.substring(5).toUpperCase() + "****");
                }

            } while (joinCheck);


            Thread imavThread = new Thread(() -> {
                if(!socket.isClosed()) {
                    try {
                        OutputStream output = socket.getOutputStream();
                        while (true) {
                            String imav = "IMAV";
                            byte[] dataToSend = imav.getBytes();
                            output.write(dataToSend);
                            Thread.sleep(30000);
                        }
                    } catch (InterruptedException | IOException e) {
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            imavThread.start();

            Thread serverInput = new Thread(() -> {
                    try {
                        InputStream input = socket.getInputStream();
                        while (!socket.isClosed()) {
                            byte[] threadDataIn = new byte[1024];
                            input.read(threadDataIn);
                            String threadMsgIn = new String(threadDataIn);
                            threadMsgIn = threadMsgIn.trim();
                            try {
                                if (threadMsgIn.substring(0, 4).equalsIgnoreCase("DATA")) {
                                    System.out.println(threadMsgIn.substring(5));
                                }
                                if (threadMsgIn.substring(0, 4).equalsIgnoreCase("LIST")) {
                                    System.out.println("Clients online: " + threadMsgIn.substring(5));
                                }
                            }catch (StringIndexOutOfBoundsException e) {
                                socket.close();
                            }
                        }
                    } catch (IOException e) {
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

            });
            serverInput.start();

            Thread checkConnection = new Thread(() -> {
                while(true) {
                    if (socket.isClosed() || !socket.isConnected()) {
                        System.out.println("CONNECTION TO SERVER LOST");
                        System.exit(0);
                    }
                }
            });
            checkConnection.start();

            System.out.println("Hello " + username);
            while (!socket.isClosed()) {
                OutputStream output = socket.getOutputStream();

                sc = new Scanner(System.in);
                System.out.println("\nPlease enter command (type HELP for available commands)");
                String commandChoice = sc.nextLine();

                switch (commandChoice) {
                    case "DATA":
                        System.out.println("\n----DATA----\nPlease enter your message of maximum 250 characters. (type BACK to return to command menu)\n");
                        String data;
                        do {
                            data = sc.nextLine();
                            if(!data.equals("BACK")) {
                                String msgToSend = "DATA " + username + ": " + data + "\n";
                                byte[] dataToSend = msgToSend.getBytes();
                                if (data.length() < 250) {
                                    output.write(dataToSend);
                                }
                            }
                            Thread.sleep(200);
                        } while (!data.equalsIgnoreCase("BACK") && data.length() < 250);
                        if (data.length() > 250) {
                            System.out.println("****INPUT CANNOT BE MORE THAN 250 CHARACTERS****");
                        } else {
                            System.out.println("****RETURNING TO COMMAND MENU****");
                        }
                        break;
                    case "QUIT":
                        try {
                            System.out.println("****QUITTING SESSION****");
                            String msgToSend = "QUIT";
                            byte[] dataToSend = msgToSend.getBytes();
                            output.write(dataToSend);
                            System.exit(0);
                        }catch (IOException e) {
                            socket.close();
                        }
                        break;
                    case "HELP":
                        System.out.println("\n----AVAILABLE CLIENT COMMANDS----\nDATA (Send data to other clients)\nQUIT (Quit the system)\n");
                        break;
                    default:
                        System.out.println("****COMMAND NOT RECOGNIZED****\n");
                        break;
                }
            }
        } catch (ConnectException e){
            System.out.println("****CONNECTION TO SERVER COULD NOT BE ESTABLISHED****");
        } catch (IOException | InterruptedException e) {
            System.out.println("****CONNECTION TO SERVER LOST****");
        }
    }

    private static boolean checkUsername(String username) {
        if (username.length() <= 12 && username.matches("[A-Za-z0-9_-]+")){
           return true;
        }else
            System.out.println("****E200: INVALID USERNAME****");
            return false;
    }
}

class Client {
    private String ipAddress;
    private int portNumber;
    private String username = " ";
    private long imav = 0;
    private Socket socket;

    Client() {
    }

    String getIpAddress() {
        return ipAddress;
    }

    void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    int getPortNumber() {
        return portNumber;
    }

    void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    long getImav() {
        return imav;
    }

    void setImav(long imav) {
        this.imav = imav;
    }

    Socket getSocket() {
        return socket;
    }

    void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return
                "ipAddress='" + ipAddress + '\'' +
                        ", portNumber=" + portNumber +
                        ", username='" + username + '\'' +
                        ", imav=" + imav +
                        '}';
    }
}