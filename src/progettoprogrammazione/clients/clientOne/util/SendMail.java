package progettoprogrammazione.clients.clientOne.util;

import java.io.IOException;
import java.io.ObjectOutputStream;


public class SendMail extends Thread{
    private ObjectOutputStream outStream;
    private Mail mail;



    public SendMail(Mail m, ObjectOutputStream out) {
        outStream = out;
        mail = m;
    }

    public void run() {
        try {
            outStream.writeObject(mail.convertMailToString());
            System.out.println("Ho inviato la mail al server");
        } catch (IOException e) {
            System.out.println("Non sono riuscito ad inviare la mail");
        }
    }
}