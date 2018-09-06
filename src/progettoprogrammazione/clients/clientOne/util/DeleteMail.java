package progettoprogrammazione.clients.clientOne.util;

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
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}