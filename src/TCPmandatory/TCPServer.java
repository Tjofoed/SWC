package TCPmandatory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class TCPServer {
    public static void main(String[] args) {
        ArrayList<Client> clientList = new ArrayList<>();

        System.out.println("=============SERVER==============");

        final int PORT_LISTEN = 5757;

        try {
            ServerSocket server = new ServerSocket(PORT_LISTEN);

            while (true) {
                System.out.println("Awaiting connection...\n");
                Socket socket = server.accept();
                Client client = new Client();
                client.setSocket(socket);
                clientList.add(client);
                System.out.println("Client connected");
                client.setIpAddress(socket.getInetAddress().getHostAddress());
                client.setPortNumber(socket.getPort());
                System.out.println("IP: " + client.getIpAddress());
                System.out.println("PORT: " + client.getPortNumber() + "\n");

                // Checks for incoming data from clients
                Thread dataFromClient = new Thread(() -> {
                    while (!socket.isClosed()) {
                        try {
                            InputStream input = socket.getInputStream();
                            OutputStream output = socket.getOutputStream();

                            byte[] dataIn = new byte[1024];

                            input.read(dataIn);
                            String msgIn = new String(dataIn);
                            msgIn = msgIn.trim();
                            String commandChoice = null;
                            try {
                                commandChoice = msgIn.substring(0, 4);
                                switch (commandChoice) {
                                    case "JOIN":
                                        String username = (msgIn.substring(5, msgIn.indexOf(",")));
                                        if (checkUsername(username)) {
                                            for (int i = 0; i < clientList.size(); i++) {
                                                if (clientList.get(i).getUsername().equalsIgnoreCase(username)) {
                                                    String msgToSend = "J_ER E100: USERNAME ALREADY EXISTS";
                                                    byte[] dataToSend = msgToSend.getBytes();
                                                    output.write(dataToSend);
                                                    break;
                                                } else {
                                                    client.setUsername(username);
                                                    String msgToSend = "J_OK";
                                                    byte[] dataToSend = msgToSend.getBytes();
                                                    output.write(dataToSend);
                                                    Thread.sleep(500);
                                                    sendClientList(clientList);
                                                    break;
                                                }
                                            }
                                        } else {
                                            String msgToSend = "J_ER E200: INVALID USERNAME";
                                            byte[] dataToSend = msgToSend.getBytes();
                                            output.write(dataToSend);
                                        }
                                        break;
                                    case "DATA":
                                        String msgToSend = msgIn;
                                        byte[] dataToSend = msgToSend.getBytes();
                                        for (int i = 0; i < clientList.size(); i++) {
                                            output = clientList.get(i).getSocket().getOutputStream();
                                            output.write(dataToSend);
                                        }
                                        if (msgToSend.substring(0, 4).equalsIgnoreCase("DATA")) {
                                            System.out.println(msgToSend.substring(5));
                                        }
                                        break;
                                    case "IMAV":
                                        long imav = (System.currentTimeMillis() / 1000);
                                        if (client.getImav() == 0) {
                                            client.setImav(imav);
                                            System.out.println(msgIn + " received from " + client.getUsername());
                                        } else {
                                            System.out.println(msgIn + " received from " + client.getUsername() + " " + (imav - client.getImav()) + " seconds after last IMAV");
                                        }
                                        client.setImav(imav);
                                        break;
                                    case "QUIT":
                                        System.out.println("CLIENT LEFT SERVER (Client: " + client.getUsername() + ")");
                                        clientList.remove(client);
                                        sendClientList(clientList);
                                        socket.close();
                                        break;
                                    default:
                                        msgToSend = "J_ER E400: Command not recognized";
                                        dataToSend = msgToSend.getBytes();
                                        output.write(dataToSend);
                                        break;
                                }
                            } catch (StringIndexOutOfBoundsException e) {
                                String msgToSend = "J_ER E400: COMMAND NOT RECOGNIZED";
                                byte[] dataToSend = msgToSend.getBytes();
                                output.write(dataToSend);
                            }
                        } catch (IOException | InterruptedException e) {
                            System.out.println("CLIENT CONNECTION CLOSED (Client: " + client.getUsername() + ")");
                            try {
                                socket.close();
                                clientList.remove(client);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                dataFromClient.start();

                Thread imavHandler = new Thread(() -> {
                    try {
                        while (!socket.isClosed()) {
                            Thread.sleep(40000);
                            for (int i = 0; i < clientList.size(); i++) {
                                long imav = (System.currentTimeMillis() / 1000);
                                // checks if imav difference is below 70sec
                                if (clientList.get(i).getImav() > 0) {
                                    if (imav - clientList.get(i).getImav() > 70) {
                                        System.out.println(clientList.get(i).getUsername() + " has been removed after being inactive for " + (imav - clientList.get(i).getImav()) + " seconds");
                                        clientList.get(i).getSocket().close();
                                        clientList.remove(i);
                                        sendClientList(clientList);
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
                imavHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkUsername(String username) {
        return username.length() <= 12 && username.matches("[A-Za-z0-9_-]+");
    }

    private static void sendClientList(ArrayList clientList) {
        ArrayList<Client> tempList = clientList;
        String msgToSend = "LIST ";
        for (int i = 0; i < tempList.size(); i++) {
            if (!tempList.get(i).getUsername().equalsIgnoreCase(" ")) {
                msgToSend += tempList.get(i).getUsername() + " ";
            }
        }
        byte[] dataToSend = msgToSend.getBytes();
        try {
            for (int i = 0; i < tempList.size(); i++) {
                OutputStream output = tempList.get(i).getSocket().getOutputStream();
                output.write(dataToSend);
            }
            if (tempList.size() == 0) {
                System.out.println("Clients online: None");
            } else {
                System.out.println("Clients online: " + msgToSend.substring(5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}