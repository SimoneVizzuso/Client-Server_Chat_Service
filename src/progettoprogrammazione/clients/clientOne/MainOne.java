package progettoprogrammazione.clients.clientOne;

import javafx.application.Application;
import java.util.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import progettoprogrammazione.clients.clientOne.model.ClientOne;
import progettoprogrammazione.clients.clientOne.model.MailViewOne;
import progettoprogrammazione.clients.clientOne.view.MainViewControllerOne;
import progettoprogrammazione.clients.clientOne.view.NewMailViewControllerOne;
import progettoprogrammazione.clients.util.ReceiveMail;

import java.io.IOException;

public class MainOne extends Application {

    private Stage primaryStage;

    private ObservableList<MailViewOne> mailData = FXCollections.observableArrayList();

    // Costruttore del main che gli passa delle mail di esempio
    public MainOne() {
    }

    // Ritorna i dati come una lista observable di mail
    public ObservableList<MailViewOne> getMailData() {
        return mailData;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Costruttore e diamo il titolo alla finestra
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Client simonevizzuso@unito.edu");

        //Mostriamo la finestra
        showMainView();
    }

    // Metodo per creare la finestra principale
    private void showMainView() throws IOException {
        // Crea un nuovo loader e carica il file FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainOne.class.getResource("view/MainViewOne.fxml"));
        BorderPane mainLayout = loader.load();

        // Crea la scene in cui mostrare la finestra
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Diamo al controller l'accesso alla classe Main
        MainViewControllerOne controller = loader.getController();
        controller.setMain(this);

        ReceiveMail.um.addObserver(controller);
    }

    // Metodo per mostrare la finestra New MailView
    public boolean showNewMailView(MailViewOne newMail) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainOne.class.getResource("view/NewMailViewOne.fxml"));
            BorderPane newMailLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Scrivi Mail");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(newMailLayout);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            NewMailViewControllerOne controller = loader.getController();
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
        ClientOne c = new ClientOne();
        c.start();
        launch(args);
    }
}
