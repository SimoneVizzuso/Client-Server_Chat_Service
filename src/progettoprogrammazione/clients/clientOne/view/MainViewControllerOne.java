package progettoprogrammazione.clients.clientOne.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import progettoprogrammazione.clients.clientOne.MainOne;
import progettoprogrammazione.clients.clientOne.model.ClientOne;
import progettoprogrammazione.clients.clientOne.model.MailViewOne;
import progettoprogrammazione.resources.Mail;

import java.util.Observable;
import java.util.Observer;

public class MainViewControllerOne implements Observer {

    @FXML
    private TableView<MailViewOne> mailTable;
    @FXML
    private TableColumn<MailViewOne, String> titleColumn;
    @FXML
    private TableColumn<MailViewOne, String> senderColumn;
    @FXML
    private TableColumn<MailViewOne, String> dateColumn;

    @FXML
    private Label senderLabel;
    @FXML
    private Label ccLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea bodyArea;

    // Riferimento alla classe Main
    private MainOne main;

    public MainViewControllerOne(){

    }

    private void showMailDetails(MailViewOne mail){
        if (mail != null){
            senderLabel.setText(mail.getSender());
            ccLabel.setText(mail.getCc());
            titleLabel.setText(mail.getTitle());
            bodyArea.setText(mail.getBody());
        }else{
            senderLabel.setText("");
            ccLabel.setText("");
            titleLabel.setText("");
            bodyArea.setText("");
        }
    }

    @FXML
    private void initialize(){
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        senderColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

        showMailDetails(null);

        mailTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> MainViewControllerOne.this.showMailDetails(newValue));
    }

    @FXML
    private void handleNewMail() {
        MailViewOne tM = new MailViewOne();
        boolean okClicked = main.showNewMailView(tM);
        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientOne.sendView(mail);
        }
    }

    @FXML
    private void forwardMail(){
        MailViewOne tM = new MailViewOne();

        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        MailViewOne temporaryMail = mailTable.getItems().get(selectedIndex);

        tM.setBody("\n==========\n" + temporaryMail.getBody());
        tM.setTitle("Fwd: " + temporaryMail.getTitle());

        boolean okClicked = main.showNewMailView(tM);

        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientOne.sendView(mail);
        }
    }

    @FXML
    private void replyMail(){
        MailViewOne tM = new MailViewOne();

        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        MailViewOne temporaryMail = mailTable.getItems().get(selectedIndex);

        tM.setBody("\n==========\n" + temporaryMail.getBody());
        tM.setReceiver(temporaryMail.getSender());
        tM.setTitle("Re: " + temporaryMail.getTitle());

        boolean okClicked = main.showNewMailView(tM);

        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientOne.sendView(mail);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void deleteMail(){
        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            mailTable.getItems().remove(selectedIndex);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Errore Selezione");
            alert.setHeaderText("Non hai selezionato nessuna mail");
            alert.setContentText("Per favore, seleziona una mail dalla tabella");
        }
    }

    // Abbiamo bisogno che ci venga passato il main, per operare da controller
    public void setMain(MainOne main) {
        this.main = main;

        mailTable.setItems(main.getMailData());
    }

    @Override
    public void update(Observable o, Object arg) {
        Mail mail = (Mail) arg;
        MailViewOne mailView= new MailViewOne();
        mailView.setSender(mail.getSender());
        mailView.setReceiver(mail.getReceiver());
        mailView.setCc(mail.getCc());
        mailView.setTitle(mail.getTitle());
        mailView.setBody(mail.getBody());
        mailView.setId(mail.getId());
        mailView.setDate(mail.getDate());

        main.getMailData().add(mailView);
    }
}