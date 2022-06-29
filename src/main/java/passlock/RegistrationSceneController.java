package passlock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RegistrationSceneController {

    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPassword;
    @FXML
    private CheckBox cbTogglePassword;
    @FXML
    private Label lblErrorMsg;

    private String password;

    public void tfTextChanged(InputMethodEvent inputMethodEvent) {
        password = tfPassword.getText();
        pfPassword.setText(password);
    }

    public void pfTextChanged(InputMethodEvent inputMethodEvent) {
        password = pfPassword.getText();
        tfPassword.setText(password);
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

    public void register(ActionEvent actionEvent) throws IOException {
        password = tfPassword.getText();
        if (!cbTogglePassword.isSelected())
            password = pfPassword.getText();

        if (password.length() < 8) {
            lblErrorMsg.setVisible(true);
            System.out.println(cbTogglePassword.isSelected());
            return;
        }

        Main.masterAccount.setHashedPassword(Encryption.hash(pfPassword.getText()));

        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("psslck.data")))) {
            stream.writeObject(Main.masterAccount);
            System.out.println("Master account creation saved!");
        } catch (IOException e) {
            System.out.println("Failed to save: " + e);
        }

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginLoader.load()));
        loginStage.getIcons().add(new Image(getClass().getResourceAsStream("Passlock.png")));
        loginStage.setTitle("Login");
        loginStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
