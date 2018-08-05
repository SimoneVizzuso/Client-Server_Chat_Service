package progettoprogrammazione.clients.util;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class DeleteMail extends Thread{
    private ObjectOutputStream outStream;
    private Long idMail;

    public DeleteMail(Long id, ObjectOutputStream out){
        outStream = out;
        idMail = id;
    }

    public void run(){
        try {
            outStream.writeObject(idMail);
            System.out.println("Ho inviato l'id della mail da eliminare al server");
        } catch (IOException e) {
            System.out.println("Non sono riuscito ad inviare l'id della mail da eliminare");
        }
    }
}