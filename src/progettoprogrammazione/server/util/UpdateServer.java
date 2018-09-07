package progettoprogrammazione.server.util;

import java.util.Observable;

public class UpdateServer extends Observable {

    public void updateConsole(String text){
        setChanged();
        notifyObservers(text + "\n");
    }
}