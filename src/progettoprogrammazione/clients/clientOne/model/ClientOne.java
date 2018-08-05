package progettoprogrammazione.clients.clientOne.model;

import progettoprogrammazione.clients.util.DeleteMail;
import progettoprogrammazione.clients.util.ReceiveMail;
import progettoprogrammazione.clients.util.SendMail;
import progettoprogrammazione.resources.Mail;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientOne extends Thread{
    private static ObjectOutputStream outStream;
    public static String user = "simonevizzuso@unito.edu";
    private final static Object sync = new Object();

    public ClientOne(){
        setDaemon(true);
    }

    private static void setOutStream(ObjectOutputStream outStream) {
        ClientOne.outStream = outStream;
    }

    public void run(){
        System.out.println("Client Online");
        boolean exit = false;
        while (!exit){
            synchronized(sync) {
                try {
                    Socket socket = new Socket("localhost", 1898);
                    System.out.println("Ho aperto la connessione con il server!");

                    setOutStream(new ObjectOutputStream(socket.getOutputStream()));

                    outStream.writeObject(user);

                    receive(socket);
                    sync.wait();
                } catch (IOException e) {
                    System.out.println("Non sono riuscito a stabilire una connessione con il server");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    exit = true;
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

    private static void receive(Socket s){
        ReceiveMail rm = new ReceiveMail(s, sync);
        rm.start();
    }

    public static void deleteView(long id){
        delete(id, outStream);
    }

    private static void delete(long id, ObjectOutputStream out){
        DeleteMail dm = new DeleteMail(id, out);
        dm.start();
    }
}
