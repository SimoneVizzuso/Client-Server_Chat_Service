package progettoprogrammazione.resources;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mail implements Serializable{
    private LocalDate data;
    private long id;
    private String sender;
    private List<String> receiver = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private List<String> ccn = new ArrayList<>();
    private String title;
    private String body;

    public Mail(){
        this(null, null, null, null, null, null, null, 0);
    }

    public Mail(String sender, String receiver, String cc, String ccn, String title, String body, LocalDate date, long id){
        this.sender = sender;
        if (receiver != null){ this.receiver.addAll(convertToList(receiver));}
        if (cc != null){ this.cc.addAll(convertToList(cc));}
        if (ccn != null){ this.ccn.addAll(convertToList(ccn));}
        this.title = title;
        this.body = body;
        this.data = date;
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return convertToString(receiver);
    }

    public List<String> getReceiverList() {
        return receiver;
    }

    public String getCc() {
        return convertToString(cc);
    }

    public List<String> getCcList() {
        return cc;
    }

    public String getCcn() { return convertToString(ccn); }

    public List<String> getCcnList() {
        return ccn;
    }

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

    private List<String> convertToList(String users) {
        if (users == null) {
            return null;
        } else {
            return Arrays.asList(users.replace(" ", "").split(",", 0));
        }
    }

    private String convertToString(List<String> users){
        return String.join(",", users);
    }

    public List<String> getAllReceiver(){
        List<String> list = new ArrayList<>();
        if (!getReceiverList().isEmpty()) {list.addAll(getReceiverList());}
        if (!getCcList().isEmpty()) {list.addAll(getCcList());}
        if (!getCcnList().isEmpty()) {list.addAll(getCcnList());}
        return list;
    }
}