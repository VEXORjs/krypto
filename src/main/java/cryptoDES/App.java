package cryptoDES;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("layout.fxml")));
        primaryStage.setTitle("DES Cryptography Tool");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }
}