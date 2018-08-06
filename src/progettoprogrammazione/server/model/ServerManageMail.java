package progettoprogrammazione.server.model;

import progettoprogrammazione.server.util.Mail;
import progettoprogrammazione.server.util.UpdateServer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerManageMail extends Thread{
    private static HashMap<String, Socket> clients = new HashMap<>();
    public static UpdateServer us = new UpdateServer();
    private String nameClient;
    private ObjectInputStream inStream;

    ServerManageMail(ObjectInputStream in, HashMap<String, Socket> c, String nC){
        inStream = in;
        clients = c;
        nameClient = nC;
        setDaemon(true);
    }

    public void run() {
        while(true){
            try {
                Object input = inStream.readObject();
                if (input instanceof ArrayList) {
                    Mail mail = new Mail();
                    mail.convertStringToMail((ArrayList<String>) input);
                    receiveMail(mail);
                } else if (input instanceof Long){
                    Long idMail = (Long) input;
                    deleteMail(idMail);
                }
            } catch (IOException e) {
                us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Si è scollegato " + nameClient + "\n");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e){
                System.out.println("Problema");
                e.printStackTrace();
                System.out.println(e.getLocalizedMessage() + "\n\n" + e.getMessage() + "\n\n" + e.toString());
            }

        }
    }

    private void savemail(Mail mail, String receiver){
        us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Sto salvando il messaggio " + mail.getId() + "\n");
        String path = "src/progettoprogrammazione/server/archive/error/";

        if (receiver != null) {
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
        // NB: Se il file esiste gia', il PrintWriter e' null
    }

    private void reSendMail(Mail mail, String receiver){
        Socket socket = null;

        if (receiver != null) {
            socket = clients.get(receiver);
        }

        ObjectOutputStream outStream;
        try {
            if (socket != null) {
                outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.writeObject(mail.convertMailToString(mail));
                us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Mail " + mail.getId() + " inviata a " + receiver + "\n");
            }
        } catch (IOException e) {
            System.out.println("Il ricevente non e' online");
            e.getStackTrace();
        }
    }

    private void errorMail(Mail mail, String receiver){
        Socket socket = clients.get(mail.getSender());

        ObjectOutputStream outStream;
        try {
            if (socket != null) {
                outStream = new ObjectOutputStream(socket.getOutputStream());
                Mail erMail = new Mail("Server", mail.getSender(), null, null, "Invio non riuscito mail " + mail.getId(), "La mail '" + mail.getTitle() + "' non è stata inviata perche' l'indirizzo mail '" + receiver + "' non esiste", LocalDate.now(), System.currentTimeMillis());
                outStream.writeObject(erMail);
                us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " La Mail " + mail.getId() + " è stata inviata a " + mail.getSender() + " perchè uno o più destinatari non esistono\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMail(Long idMail){
        String path = ("src/progettoprogrammazione/server/archive/" + nameClient + "/");
        File file = new File(path + idMail + ".txt");

        if(file.delete()){
            System.out.println("Mail eliminata");
        } else {
            System.out.println("Mail non eliminata");
        }
    }

    private void receiveMail(Mail mail){
        if (mail.getAllReceiver() != null) {
            for (String receiver : mail.getAllReceiver()) {
                if (new File("src/progettoprogrammazione/server/archive/" + receiver).exists()) {
                    us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " È arrivato un nuovo messaggio da " + mail.getSender() + " a " + receiver + "\n");
                    savemail(mail, receiver);
                } else {
                    if (!receiver.equals("")) {
                        errorMail(mail, receiver);
                    }
                }

                if (clients.containsKey(receiver)) {
                    us.updateConsole(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " Invio il messaggio " + mail.getId() + " a " + receiver + "\n");
                    reSendMail(mail, receiver);
                }
            }
        }
    }
}