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
        uploadMail();
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
                us.updateConsole("Si è disconnesso " + nameClient);
                clients.remove(nameClient);
                break;
            } catch (ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadMail (){
        File folder = new File("src/progettoprogrammazione/server/archive/" + nameClient + "/");
        File[] listOfFiles = folder.listFiles();
        ObjectOutputStream outStream;
        Socket socket = clients.get(nameClient);

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                try {
                    FileInputStream fileIn = new FileInputStream(listOfFile);
                    ObjectInputStream in = new ObjectInputStream(fileIn);

                    Mail mail = (Mail) in.readObject();

                    outStream = new ObjectOutputStream(socket.getOutputStream());
                    outStream.writeObject(mail.convertMailToString());
                    outStream.flush();

                    in.close();
                    fileIn.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveMail(Mail mail, String receiver){
        us.updateConsole("Sto salvando il messaggio " + mail.getId());
        String path = ("src/progettoprogrammazione/server/archive/" + receiver + "/");

        try {
            FileOutputStream fileOut = new FileOutputStream(path + mail.getStringId() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mail);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // NB: Se il file esiste gia', il PrintWriter e' null
    }

    private void receiveMail(Mail mail){
        if (mail.getAllReceiver() != null) {
            for (String receiver : mail.getAllReceiver()) {
                if (new File("src/progettoprogrammazione/server/archive/" + receiver).exists()) {
                    us.updateConsole("È arrivato un nuovo messaggio da " + mail.getSender() + " a " + receiver);
                    saveMail(mail, receiver);
                } else {
                    if (!receiver.equals("")) {
                        errorMail(mail, receiver);
                    }
                }

                if (clients.containsKey(receiver)) {
                    sendMail(mail, receiver);
                }
            }
        }
    }

    private void sendMail(Mail mail, String receiver){
        Socket socket = clients.get(receiver);

        ObjectOutputStream outStream;
        try {
            if (socket != null) {
                outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.writeObject(mail.convertMailToString());
                us.updateConsole( "Mail " + mail.getId() + " inviata a " + receiver);
            }
        } catch (IOException e){
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
                us.updateConsole("La Mail " + mail.getId() + " è stata inviata a " + mail.getSender() + " perchè uno o più destinatari non esistono");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMail(Long idMail){
        String path = ("src/progettoprogrammazione/server/archive/" + nameClient + "/");
        File file = new File(path + idMail + ".ser");

        if(file.delete()){
            us.updateConsole("Ho eliminato la mail " + idMail + " di " + nameClient);
        }
    }
}