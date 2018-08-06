package progettoprogrammazione.server.util;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mail implements Serializable{
    private String sender;
    private List<String> receiver = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private List<String> ccn = new ArrayList<>();
    private String title;
    private String body;
    private LocalDate data;
    private long id;

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

    private void setSender(String s) {
        sender = s;
    }

    public String getReceiver() {
        return convertToString(receiver);
    }

    private void setReceiver(String r) {
        receiver = convertToList(r);
    }

    public List<String> getReceiverList() {
        return receiver;
    }

    public String getCc() {
        return convertToString(cc);
    }

    private void setCc(String c) {
        cc = convertToList(c);
    }

    public List<String> getCcList() {
        return cc;
    }

    public String getCcn() { return convertToString(ccn); }

    private void setCcn(String cn) {
        ccn = convertToList(cn);
    }

    public List<String> getCcnList() {
        return ccn;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String t) {
        title = t;
    }

    public String getBody() {
        return body;
    }

    private void setBody(String b) {
        body = b;
    }

    public LocalDate getDate() {
        return data;
    }

    private void setData(String d) {
        data = LocalDate.parse(d);
    }

    public long getId() {
        return id;
    }

    private void setId(String i) { id = Long.parseLong(i);}

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

    public ArrayList<String> convertMailToString(){
        ArrayList<String> export = new ArrayList<>();
        export.add(getSender());
        export.add(getReceiver());
        export.add(getCc());
        export.add(getCcn());
        export.add(getTitle());
        export.add(getBody());
        export.add(getDate().toString());
        export.add(String.valueOf(getId()));
        return export;
    }

    public void convertStringToMail(ArrayList<String> input) {
        setSender(input.get(0));
        setReceiver(input.get(1));
        if (!input.get(2).equals("")){
            setCc(input.get(2));
        }
        if (!input.get(3).equals("")){
            setCcn(input.get(3));
        }
        setTitle(input.get(4));
        setBody(input.get(5));
        setData(input.get(6));
        setId(input.get(7));
    }
}