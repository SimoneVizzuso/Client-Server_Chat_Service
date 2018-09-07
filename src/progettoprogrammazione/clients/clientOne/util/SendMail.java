package progettoprogrammazione.clients.clientOne.util;

import progettoprogrammazione.clients.clientOne.view.MainViewControllerOne;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static javax.swing.JOptionPane.showMessageDialog;


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}