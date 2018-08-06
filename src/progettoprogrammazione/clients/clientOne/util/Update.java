package progettoprogrammazione.clients.clientOne.util;

import java.util.Observable;

public class Update extends Observable {

    void updateView(Mail mail){
        setChanged();
        notifyObservers(mail);
    }
}