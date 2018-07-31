package progettoprogrammazione.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import progettoprogrammazione.server.model.Server;
import progettoprogrammazione.server.model.ServerManageMail;
import progettoprogrammazione.server.view.ServerViewController;

import java.io.IOException;

public class MainServer extends Application {

    private Stage primaryStage;

    public MainServer(){ }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Server");

        showServerView();
    }

    private void showServerView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainServer.class.getResource("view/ServerView.fxml"));
        BorderPane mainLayout = loader.load();

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        ServerViewController controller = loader.getController();
        controller.setMain(this);

        Server.us.addObserver(controller);
        ServerManageMail.us.addObserver(controller);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
