package passlock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static MasterAccount masterAccount;
    public static FXMLLoader mainLoader;

    @Override
    public void start(Stage stage) throws IOException {
        masterAccount = new MasterAccount();

        mainLoader = new FXMLLoader(getClass().getResource("mainscene.fxml"));

        FXMLLoader loader;

        Path masterpassFile = Paths.get("psslck.data");

        if (!Files.exists(masterpassFile)) {
            loader = new FXMLLoader(Main.class.getResource("registrationScene.fxml"));
            stage.setTitle("Register");
        }
        else {
            loader = new FXMLLoader(Main.class.getResource("loginScene.fxml"));
            stage.setTitle("Login");
        }

        Scene scene = new Scene(loader.load());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Passlock.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}