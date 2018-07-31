package progettoprogrammazione.server.model;

import progettoprogrammazione.resources.Mail;
import progettoprogrammazione.server.util.UpdateServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ServerManageMail extends Thread{
    private ObjectInputStream inStream;
    private static HashMap<String, Socket> clients = new HashMap<>();
    public static UpdateServer us = new UpdateServer();
    private String nameClient;

    ServerManageMail(ObjectInputStream in, HashMap<String, Socket> c, String nC){
        inStream = in;
        clients = c;
        nameClient = nC;
        setDaemon(true);
    }

    public void run() {
        Mail mail;
        while(true){
            try {
                mail = (Mail) inStream.readObject();
                us.updateConsole("È arrivato un nuovo messaggio da " + mail.getSender() + " per " + mail.getReceiver() + "\n");
                savemail(mail);
                reSendMail(mail);
            } catch (IOException e) {
                us.updateConsole("Si è scollegato " + nameClient + "\n");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void savemail(Mail mail){
        us.updateConsole("Sto salvando la mail " + mail.getId() + "\n");
        String path = "src/progettoprogrammazione/server/archive/error/";

        if (mail.getReceiver() != null){
            path = ("src/progettoprogrammazione/server/server/archive/" + mail.getReceiver() + "/");
        }

        try (PrintWriter p = new PrintWriter(new File(path + mail.getStringId() + ".txt"))) {
            p.println(mail.getId());
            p.println(mail.getSender());
            p.println(mail.getReceiver());
            p.println(mail.getTitle());
            p.println(mail.getBody());
            p.println(mail.getDate());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // NB: Se il file esiste gia', il PrintWriter e' null
    }

    private void reSendMail(Mail mail){
        us.updateConsole("Sto mandando la mail " + mail.getId() + " a " + mail.getReceiver() + "\n");
        Socket socket = null;

        if (mail.getReceiver() != null){
            socket = clients.get(mail.getReceiver());
        }

        ObjectOutputStream out;
        try {
            if (socket != null) {
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(mail);
                us.updateConsole("Mail " + mail.getId() + " inviata\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}