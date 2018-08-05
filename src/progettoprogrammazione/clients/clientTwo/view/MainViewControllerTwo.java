package progettoprogrammazione.clients.clientTwo.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import progettoprogrammazione.clients.clientTwo.MainTwo;
import progettoprogrammazione.clients.clientTwo.model.ClientTwo;
import progettoprogrammazione.clients.clientTwo.model.MailViewTwo;
import progettoprogrammazione.resources.Mail;

import java.util.Observable;
import java.util.Observer;

public class MainViewControllerTwo implements Observer {

    @FXML
    private TableView<MailViewTwo> mailTable;
    @FXML
    private TableColumn<MailViewTwo, String> titleColumn;
    @FXML
    private TableColumn<MailViewTwo, String> senderColumn;
    @FXML
    private TableColumn<MailViewTwo, String> dateColumn;

    @FXML
    private Label senderLabel;
    @FXML
    private Label receiverLabel;
    @FXML
    private Label ccLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea bodyArea;

    // Riferimento alla classe Main
    private MainTwo main;
    private String user = ClientTwo.user;

    public MainViewControllerTwo(){

    }

    private void showMailDetails(MailViewTwo mail){
        if (mail != null){
            senderLabel.setText(mail.getSender());
            receiverLabel.setText(mail.getReceiver());
            ccLabel.setText(mail.getCc());
            titleLabel.setText(mail.getTitle());
            bodyArea.setText(mail.getBody());
        }else{
            senderLabel.setText("");
            receiverLabel.setText("");
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

        mailTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> MainViewControllerTwo.this.showMailDetails(newValue));
    }

    @FXML
    private void handleNewMail() {
        MailViewTwo tM = new MailViewTwo();
        boolean okClicked = main.showNewMailView(tM);
        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientTwo.sendView(mail);
        }
    }

    @FXML
    private void handleForward(){
        MailViewTwo tM = new MailViewTwo();

        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        MailViewTwo temporaryMail = mailTable.getItems().get(selectedIndex);

        tM.setBody("\n==========\n" + temporaryMail.getBody());
        tM.setTitle("Fwd: " + temporaryMail.getTitle());

        boolean okClicked = main.showNewMailView(tM);

        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientTwo.sendView(mail);
        }
    }

    @FXML
    private void handleReply(){
        MailViewTwo tM = new MailViewTwo();

        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        MailViewTwo temporaryMail = mailTable.getItems().get(selectedIndex);

        tM.setBody("\n==========\n" + temporaryMail.getBody());
        tM.setReceiver(temporaryMail.getSender());
        tM.setTitle("Re: " + temporaryMail.getTitle());

        boolean okClicked = main.showNewMailView(tM);

        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientTwo.sendView(mail);
        }
    }

    @FXML
    private void handleReplyAll(){
        MailViewTwo tM = new MailViewTwo();

        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        MailViewTwo temporaryMail = mailTable.getItems().get(selectedIndex);

        tM.setBody("\n==========\n" + temporaryMail.getBody());
        tM.setReceiver(temporaryMail.getSender());
        tM.setCc(temporaryMail.getAllReceiver(user));
        tM.setTitle("Re: " + temporaryMail.getTitle());

        boolean okClicked = main.showNewMailView(tM);

        if (okClicked) {
            //main.getMailData().add(tM);
            Mail mail = new Mail(tM.getSender(), tM.getReceiver(), tM.getCc(), tM.getCcn(), tM.getTitle(), tM.getBody(), tM.getDate(), tM.getId());
            ClientTwo.sendView(mail);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleDelete(){
        int selectedIndex = mailTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            MailViewTwo temporaryMail = mailTable.getItems().get(selectedIndex);
            if (!temporaryMail.getSender().equals("Server")) {
                ClientTwo.deleteView(temporaryMail.getId());
            }
            mailTable.getItems().remove(selectedIndex);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Errore Selezione");
            alert.setHeaderText("Non hai selezionato nessuna mail");
            alert.setContentText("Per favore, seleziona una mail dalla tabella");
            alert.showAndWait();
        }
    }

    // Abbiamo bisogno che ci venga passato il main, per operare da controller
    public void setMain(MainTwo main) {
        this.main = main;

        mailTable.setItems(main.getMailData());
    }

    @Override
    public void update(Observable o, Object arg) {
        Mail mail = (Mail) arg;
        MailViewTwo mailView= new MailViewTwo();
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