package progettoprogrammazione.server.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import progettoprogrammazione.server.MainServer;
import progettoprogrammazione.server.model.Server;

import java.util.Observable;
import java.util.Observer;

public class ServerViewController implements Observer {

    private MainServer main;

    @FXML
    public TextArea console;

    @FXML
    public Button StartButton;

    public ServerViewController(){ }

    @FXML
    public void initialize(){
        console.setText(null);
    }

    public void setMain(MainServer main) {
        this.main = main;
    }

    @FXML
    private void handleCancel() {
        main.getPrimaryStage().close();
        System.exit(0);
    }

    @FXML
    private void handleStart() {
        Server s = new Server();
        s.start();
        StartButton.setDisable(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        String text = (String) arg;
        console.appendText(text);
    }
}