package progettoprogrammazione.server.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;

public class UpdateServer extends Observable {

    public void updateConsole(String text){
        setChanged();
        notifyObservers(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + text + "\n");
    }
}