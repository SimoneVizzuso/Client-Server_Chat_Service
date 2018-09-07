package progettoprogrammazione.clients.clientOne.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import progettoprogrammazione.clients.clientOne.model.ClientOne;
import progettoprogrammazione.clients.clientOne.model.MailViewOne;

import javax.swing.*;
import java.time.LocalDate;

import static javax.swing.JOptionPane.showMessageDialog;

public class NewMailViewControllerOne {

    @FXML
    private TextField receiverField;
    @FXML
    private TextField ccField;
    @FXML
    private TextField ccnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea bodyArea;

    private Stage newMailStage;
    private MailViewOne newMail;
    private boolean sendClicked = false;

    @FXML
    private void initialize(){

    }

    public void setNewMailStage(Stage newMailStage){
        this.newMailStage = newMailStage;
    }

    public void setNewMail(MailViewOne newMail){
        this.newMail = newMail;

        receiverField.setText(newMail.getReceiver());
        ccField.setText(newMail.getCc());
        ccnField.setText(newMail.getCcn());
        titleField.setText(newMail.getTitle());
        bodyArea.setText(newMail.getBody());
    }

    public boolean isSendClicked(){
        return sendClicked;
    }

    @FXML
    private void handleOk(){
        if (ClientOne.connect) {
            if (isInputValid()) {
                newMail.setSender("simonevizzuso@unito.edu");
                newMail.setReceiver(receiverField.getText());
                if (ccField.getText() != null) {
                    newMail.setCc(ccField.getText());
                }
                if (ccnField.getText() != null) {
                    newMail.setCc(ccnField.getText());
                }
                newMail.setTitle(titleField.getText());
                newMail.setBody(bodyArea.getText());
                newMail.setDate(LocalDate.now());
                newMail.setId(System.currentTimeMillis());
                sendClicked = true;
                newMailStage.close();
            }
        } else {
            alarmNoConnection();
            newMailStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        newMailStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (receiverField.getText() == null || receiverField.getText().length() == 0) {
            errorMessage += "Manca il Destinatario\n";
        }

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "Manca il Titolo\n";
        }

        if (bodyArea.getText() == null || bodyArea.getText().length() == 0) {
            errorMessage += "Manca il Corpo del Messaggio\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(newMailStage);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Per favore, correggere gli errori");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void alarmNoConnection(){
        showMessageDialog(null , "Il client non è connesso!", "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
