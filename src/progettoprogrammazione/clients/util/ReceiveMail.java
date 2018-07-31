package progettoprogrammazione.clients.util;

import progettoprogrammazione.resources.Mail;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiveMail extends Thread{
    public static Update um = new Update();
    private Socket socket;

    public ReceiveMail(Socket s){
        socket = s;
        setDaemon(true);
    }

    public void run() {
        while(true){
            Mail mail;
            try{
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                mail = (Mail) inStream.readObject();
                um.updateView(mail);
                saveMail(mail);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Sono stato scollegato dal server    " + e.getLocalizedMessage());
                break;
            }
        }
    }

    private void saveMail(Mail mail) {
        System.out.println("Sto salvando la mail\n");
        String path = "";

        switch (mail.getReceiver()) {
            case "simonevizzuso@unito.edu":
                path = ("src/progettoprogrammazione/clients/clientOne/archive/");
                break;
            case "gabrielelavorato@unito.edu":
                path = ("src/progettoprogrammazione/clients/clientTwo/archive/");
                break;
            case "pippobaudo@unito.edu":
                path = ("src/progettoprogrammazione/clients/clientThree/archive/");
                break;
        }
        boolean mkdirs = (new File(path)).mkdirs();

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
}