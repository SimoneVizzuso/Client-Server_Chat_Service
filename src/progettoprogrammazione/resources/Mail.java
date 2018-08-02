package progettoprogrammazione.resources;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Mail implements Serializable{
    private LocalDate data;
    private long id;
    private String sender;
    private String receiver;
    private String cc;
    private String ccn;
    private String title;
    private String body;

    public Mail(){
        this(null, null, null, null, null, null, null, 0);
    }

    public Mail(String sender, String receiver, String cc, String ccn, String title, String body, LocalDate date, long id){
        this.sender = sender;
        this.receiver = receiver;
        this.cc = cc;
        this.ccn = ccn;
        this.title = title;
        this.body = body;
        this.data = date;
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCc() {
        return cc;
    }

    public String getCcn() { return ccn; }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public LocalDate getDate() {
        return data;
    }

    public long getId() {
        return id;
    }

    public String getStringId(){
        return Long.toString(id);
    }
}