package progettoprogrammazione.clients.util;

import progettoprogrammazione.resources.Mail;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendMail extends Thread{
    private ObjectOutputStream outStream;
    private Mail mail;

    public SendMail(Mail m, ObjectOutputStream out){
        outStream = out;
        mail = m;
    }

    public void run(){
        try {
            outStream.writeObject(mail);
            System.out.println("Ho inviato la mail al server");
        } catch (IOException e) {
            System.out.println("Non sono riuscito ad inviare la mail");
        }
    }
}