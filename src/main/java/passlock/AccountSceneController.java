package passlock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AccountSceneController {

    @FXML
    private TextField tfPassword;
    @FXML
    private Button btnRemove;
    @FXML
    private CheckBox cbTogglePassword;
    @FXML
    private TextField pfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private Label lblSitename;

    public void copyUsername() {
        copyToClippboard(tfUsername.getText());
    }

    public void copyPassword() {
        copyToClippboard(pfPassword.getText());
    }

    public void setPassword(String decryptedPassword) {
        tfPassword.setText(decryptedPassword);
        pfPassword.setText(decryptedPassword);
    }

    public void setUsername(String username) {
        tfUsername.setText(username);
    }

    public void setSitename(String sitename) {
        lblSitename.setText(sitename);
    }

    public void togglePassword() {
        String password;
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

    public void copyToClippboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        board.setContents(stringSelection, null);
    }

    public void removeAccount() {
        Main.accounts.removeIf(acc -> tfUsername.getText().equals(acc.getUsername()) && lblSitename.getText().equals(acc.getSitename()) && pfPassword.getText().equals(Encryption.decode(acc.getPassword())));

        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("accounts.data")))) {
            stream.writeObject(Main.accounts);
            System.out.println("Account removed!");
            System.out.println("Saved!");
        } catch (IOException e) {
            System.out.println("Failed to save: "  + e);
        }
    }

    public void mouseHovered(MouseEvent mouseEvent) {
        btnRemove.setVisible(true);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        btnRemove.setVisible(false);
    }
}
