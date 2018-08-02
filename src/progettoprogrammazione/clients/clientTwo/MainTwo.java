package progettoprogrammazione.clients.clientTwo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import progettoprogrammazione.clients.clientTwo.model.ClientTwo;
import progettoprogrammazione.clients.clientTwo.model.MailViewTwo;
import progettoprogrammazione.clients.clientTwo.view.MainViewControllerTwo;
import progettoprogrammazione.clients.clientTwo.view.NewMailViewControllerTwo;
import progettoprogrammazione.clients.util.ReceiveMail;

import java.io.IOException;

public class MainTwo extends Application {

    private Stage primaryStage;

    private ObservableList<MailViewTwo> mailData = FXCollections.observableArrayList();

    // Costruttore del main che gli passa delle mail di esempio
    public MainTwo() {
    }

    // Ritorna i dati come una lista observable di mail
    public ObservableList<MailViewTwo> getMailData() {
        return mailData;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Costruttore e diamo il titolo alla finestra
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Client gabrielelavorato@unito.edu");

        //Mostriamo la finestra
        showMainView();
    }

    // Metodo per creare la finestra principale
    private void showMainView() throws IOException {
        // Crea un nuovo loader e carica il file FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainTwo.class.getResource("view/MainViewTwo.fxml"));
        BorderPane mainLayout = loader.load();

        // Crea la scene in cui mostrare la finestra
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Diamo al controller l'accesso alla classe Main
        MainViewControllerTwo controller = loader.getController();
        controller.setMain(this);

        ReceiveMail.um.addObserver(controller);
    }

    // Metodo per mostrare la finestra New MailView
    public boolean showNewMailView(MailViewTwo newMail) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainTwo.class.getResource("view/NewMailViewTwo.fxml"));
            BorderPane newMailLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Scrivi Mail");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(newMailLayout);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            NewMailViewControllerTwo controller = loader.getController();
            controller.setNewMailStage(dialogStage);
            controller.setNewMail(newMail);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isSendClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        ClientTwo c = new ClientTwo();
        c.start();
        launch(args);
    }
}
