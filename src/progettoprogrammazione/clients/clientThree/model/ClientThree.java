package progettoprogrammazione.clients.clientThree.model;

import progettoprogrammazione.clients.util.ReceiveMail;
import progettoprogrammazione.clients.util.SendMail;
import progettoprogrammazione.resources.Mail;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientThree extends Thread{
    private static volatile boolean connectionState = false;
    private static ObjectOutputStream outStream;
    public static String user = "pippobaudo@unito.edu";

    public ClientThree(){
        setDaemon(true);
    }

    private static void setOutStream(ObjectOutputStream outStream) {
        ClientThree.outStream = outStream;
    }

    public void run(){
        System.out.println("Client Online");
        while (!connectionState){
            try {
                Socket socket = new Socket("localhost", 1898);
                System.out.println("Ho aperto la connessione con il server!");
                connectionState = true;

                setOutStream(new ObjectOutputStream(socket.getOutputStream()));

                outStream.writeObject(user);

                receive(socket);
                while (connectionState) {
                    Thread.onSpinWait();
                }
            } catch (IOException e) {
                System.out.println("Non sono riuscito a stabilire una connessione con il server");
                try {
                    System.out.println("Proverò ogni 5 secondi ad effettuare una nuova connessione");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void sendView(Mail mail){
        send(mail, outStream);
    }

    private static void send(Mail mail, ObjectOutputStream out){
        SendMail sm = new SendMail(mail, out);
        sm.start();
    }

    private static void receive(Socket s) {
        ReceiveMail rm = new ReceiveMail(s);
        rm.start();
    }
}
