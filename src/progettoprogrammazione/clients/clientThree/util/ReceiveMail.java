package progettoprogrammazione.clients.clientThree.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ReceiveMail extends Thread{
    private boolean exit = false;
    public static Update um = new Update();
    private Socket socket;
    private final Object sync;

    public ReceiveMail(Socket s, Object sync){
        this.sync = sync;
        socket = s;
        setDaemon(true);
    }

    public void run() {
        while(!exit){
            try{
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                Object input = inStream.readObject();
                if (input instanceof ArrayList) {
                    Mail mail = new Mail();
                    mail.convertStringToMail((ArrayList<String>) input);
                    um.updateView(mail);
                }
            } catch (IOException e) {
                e.getStackTrace();
                System.out.println("Sono stato scollegato dal server ");
                synchronized (sync) {
                    sync.notify();
                }
                exit = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}