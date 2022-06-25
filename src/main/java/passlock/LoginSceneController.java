package passlock;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginSceneController {

    public PasswordField pfPassword;
    public TextField tfPassword;
    public CheckBox cbTogglePassword;
    public Label lblErrorMsg;

    private String password = "";

    public void login(ActionEvent actionEvent) {
        password = tfPassword.getText();
        if (!cbTogglePassword.isSelected())
            password = pfPassword.getText();

        if (password.length() < 8) {
            lblErrorMsg.setVisible(true);
            System.out.println(cbTogglePassword.isSelected());
            return;
        }

        Path file = Paths.get("masterpass.data");

        Master masterAcc;

        if (Files.exists(file)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))) {
                masterAcc = (Master) stream.readObject();
                System.out.println("Master password loaded!");

                if (!Encryption.hash(password).equals(masterAcc.getHasdhedPassword())) {
                    lblErrorMsg.setVisible(true);
                    return;
                }

                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainscene.fxml"));
                Stage mainStage = new Stage();

                mainStage.setOnCloseRequest(e -> {
                    e.consume();
                    mainLoader.<MainSceneController>getController().exit();
                });

                mainStage.setScene(new Scene(mainLoader.load()));
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Passlock.png")));
                mainStage.setTitle("Passlock");
                mainStage.show();

                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                System.out.println("Failed to load: " + e);
            }
        }
    }

    public void togglePassword(ActionEvent actionEvent) {
        if (cbTogglePassword.isSelected()) {
            password = pfPassword.getText();
            tfPassword.setText(password);
            tfPassword.toFront();
            return;
        }
        password = tfPassword.getText();
        pfPassword.setText(password);
        pfPassword.toFront();
    }
}
