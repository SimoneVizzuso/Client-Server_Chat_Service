package progettoprogrammazione.clients.clientTwo.model;

import progettoprogrammazione.clients.clientTwo.util.DeleteMail;
import progettoprogrammazione.clients.clientTwo.util.Mail;
import progettoprogrammazione.clients.clientTwo.util.ReceiveMail;
import progettoprogrammazione.clients.clientTwo.util.SendMail;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientTwo extends Thread{
    private static ObjectOutputStream outStream;
    public static String user = "gabrielelavorato@unito.edu";
    private final static Object sync = new Object();
    public static boolean connect = false;

    public ClientTwo(){
        setDaemon(true);
    }

    private static void setOutStream(ObjectOutputStream outStream) {
        ClientTwo.outStream = outStream;
    }

    public void run(){

        boolean exit = false;
        while (!exit) {
            try {
                synchronized (sync) {
                    Socket socket = new Socket("localhost", 1898);

                    setOutStream(new ObjectOutputStream(socket.getOutputStream()));

                    outStream.writeObject(user);
                    outStream.flush();

                    connect = true;

                    receive(socket);
                    sync.wait();
                }
            } catch (IOException e) {
                connect = false;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (InterruptedException e) {
                connect = false;
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
