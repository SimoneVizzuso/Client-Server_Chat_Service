package progettoprogrammazione.server.model;

import progettoprogrammazione.resources.Mail;
import progettoprogrammazione.server.util.UpdateServer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
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
                if (mail.getAllReceiver() != null) {
                    for (String receiver : mail.getAllReceiver()) {
                        if (clients.containsKey(receiver)) {
                            us.updateConsole("È arrivato un nuovo messaggio da " + mail.getSender() + " a " + mail.getReceiver());
                            savemail(mail);
                            if (mail.getCc() != null) {
                                us.updateConsole(" e a i Cc " + mail.getCc());
                            }
                            if (mail.getCcn() != null) {
                                us.updateConsole(" e ai Ccn " + mail.getCcn() + "\n");
                            }
                            reSendMail(mail);
                        } else {
                            errorMail(mail, receiver);
                        }
                    }
                }
            } catch (IOException e) {
                us.updateConsole("Si è scollegato " + nameClient + "\n");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void savemail(Mail mail){
        for (String receiver:mail.getAllReceiver()){
            us.updateConsole("Sto salvando la mail " + mail.getId() + "\n");
            String path = "src/progettoprogrammazione/server/archive/error/";

            if (mail.getReceiver() != null) {
                path = ("src/progettoprogrammazione/server/archive/" + receiver + "/");
            }

            try (PrintWriter p = new PrintWriter(new File(path + mail.getStringId() + ".txt"))) {
                p.println(mail.getId());
                p.println(mail.getSender());
                p.println(mail.getReceiver());
                p.println(mail.getCc());
                p.println(mail.getTitle());
                p.println(mail.getBody());
                p.println(mail.getDate());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        // NB: Se il file esiste gia', il PrintWriter e' null
    }

    private void reSendMail(Mail mail){
        us.updateConsole("Sto mandando la mail " + mail.getId() + " a " + mail.getReceiver());
        if (mail.getCc() != null){
            us.updateConsole(" e a i Cc " + mail.getCc());
        }
        if (mail.getCcn() != null){
            us.updateConsole(" e ai Ccn "+  mail.getCcn() + "\n");
        }
        for (String receiver:mail.getAllReceiver()) {
            Socket socket = null;

            if (receiver != null) {
                socket = clients.get(receiver);
            }

            ObjectOutputStream out;
            try {
                if (socket != null) {
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(mail);
                    us.updateConsole("Mail " + mail.getId() + " inviata a " + receiver + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void errorMail(Mail mail, String receiver){
        Socket socket = clients.get(mail.getSender());

        ObjectOutputStream out;
        try {
            if (socket != null) {
                out = new ObjectOutputStream(socket.getOutputStream());
                Mail erMail = new Mail("Server", mail.getSender(), null, null, "Invio non riuscito mail num " + mail.getId(), "La mail " + mail.getTitle() + "non è stata inviata perche' l'indirizzo mail " + receiver + "non esiste", LocalDate.now(), System.currentTimeMillis());
                out.writeObject(erMail);
                us.updateConsole("Mail " + mail.getId() + " inviata a " + mail.getSender() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}