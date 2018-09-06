package progettoprogrammazione.clients.clientOne.model;

import progettoprogrammazione.clients.clientOne.util.DeleteMail;
import progettoprogrammazione.clients.clientOne.util.Mail;
import progettoprogrammazione.clients.clientOne.util.ReceiveMail;
import progettoprogrammazione.clients.clientOne.util.SendMail;

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
        boolean exit = false;
        while(!exit) {
            try{
                synchronized (sync) {
                    Socket socket = new Socket("localhost", 1898);

                    setOutStream(new ObjectOutputStream(socket.getOutputStream()));

                    outStream.writeObject(user);
                    outStream.flush();

                    receive(socket);

                    sync.wait();
                }
            } catch (IOException e) {
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

    public static void sendView(Mail mail){
        send(mail, outStream);
    }

    private static void send(Mail mail, ObjectOutputStream out){
        SendMail sm = new SendMail(mail, out);
        sm.start();
    }

    private static void receive(Socket socket){
        ReceiveMail rm = new ReceiveMail(socket, sync);
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
