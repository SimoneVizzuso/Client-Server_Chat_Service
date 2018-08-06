package progettoprogrammazione.server.model;

import progettoprogrammazione.server.util.UpdateServer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Server extends Thread{

    private static final int PORT = 1898;

    public static UpdateServer us = new UpdateServer();

    private static final HashMap<String, Socket> clients = new HashMap<>();

    public Server(){
        setDaemon(true);
    }

    public void run(){
        ServerSocket listener = null;
        try{
            listener = new ServerSocket(PORT); //create new ServerSocket

            us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Server online!\n");

            while (true){
                Socket socket = listener.accept(); //block, wait for client
                Handler clientHandler = new Handler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Handler extends Thread{
        private Socket socket;

        Handler(Socket s){
            socket = s;
        }

        public void run(){
            try{
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                String nameClient = (String) inStream.readObject();

                synchronized (clients) {
                    if (!clients.containsKey(nameClient)) {
                        clients.put(nameClient, socket);
                    }
                }
                (new File("src/progettoprogrammazione/server/archive/" + nameClient)).mkdirs();

                us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Si è collegato " + nameClient + "\n");
                receive(inStream, nameClient);
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        private void receive(ObjectInputStream in, String nameClient){
            ServerManageMail rm = new ServerManageMail(in, clients, nameClient);
            rm.start();
        }
    }
}
