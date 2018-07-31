package progettoprogrammazione.clients.util;

import progettoprogrammazione.resources.Mail;

import java.util.Observable;

public class Update extends Observable {

    void updateView(Mail mail){
        setChanged();
        notifyObservers(mail);
    }
}