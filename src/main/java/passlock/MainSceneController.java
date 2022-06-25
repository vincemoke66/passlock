package passlock;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private TextField tfSearch;
    @FXML
    private Label lblErrorMsg;
    @FXML
    private CheckBox cbTogglePassword;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfSitename;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    public VBox vboxAccountContainer;

    private String password = "";

    public void search() throws IOException {
        String toFind = tfSearch.getText().toLowerCase();

        vboxAccountContainer.getChildren().clear();

        for (Account acc :
                Main.accounts) {
            String sitename = acc.getSitename().toLowerCase();
            String username = acc.getUsername().toLowerCase();

            boolean isSitenameSubstr = false;
            boolean isUsernameSubstr = false;

            if (sitename.length() >= toFind.length())
                isSitenameSubstr = toFind.equals(sitename.substring(0, toFind.length()));
            if (username.length() >= toFind.length())
                isUsernameSubstr = toFind.equals(username.substring(0, toFind.length()));

            if (isSitenameSubstr || isUsernameSubstr) {
                System.out.println("A substring");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("accountScene.fxml"));
                Parent root = loader.load();

                ((AccountSceneController) loader.getController()).setSitename(acc.getSitename());
                ((AccountSceneController) loader.getController()).setUsername(acc.getUsername());
                ((AccountSceneController) loader.getController()).setPassword(Encryption.decode(acc.getPassword()));

                vboxAccountContainer.getChildren().add(root);
            }
        }
    }

    public void exit() {
        Platform.exit();
    }

    public void togglePassword() {
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

    public void generatePassword() {
        password = Generator.generateAlphanumeric(12);
        tfPassword.setText(password);
        pfPassword.setText(password);
    }

    public void createAccount() throws IOException {
        if (tfSitename.getText().length() == 0 || tfUsername.getText().length() == 0 || pfPassword.getText().length() < 8) {
            lblErrorMsg.setTextFill(Color.web("#F96868"));
            lblErrorMsg.setVisible(true);
            return;
        }

        Account newAccount = new Account(tfSitename.getText(), tfUsername.getText(), Encryption.encode(pfPassword.getText()));
        Main.accounts.add(newAccount);

        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("accounts.data")))) {
            stream.writeObject(Main.accounts);
            System.out.println("Saved!");
        } catch (IOException e) {
            System.out.println("Failed to save: "  + e);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("accountscene.fxml"));
        Parent root = loader.load();

        ((AccountSceneController) loader.getController()).setSitename(newAccount.getSitename());
        ((AccountSceneController) loader.getController()).setUsername(newAccount.getUsername());
        ((AccountSceneController) loader.getController()).setPassword(Encryption.decode(newAccount.getPassword()));

        vboxAccountContainer.getChildren().add(root);
        tfPassword.clear();
        pfPassword.clear();
        tfSitename.clear();
        tfUsername.clear();
    }

    public void refresh() throws IOException {
        loadAllAcounts();
    }

    public void clear() throws IOException {
        tfSearch.clear();
        loadAllAcounts();
    }

    public void loadAllAcounts() throws IOException {
        vboxAccountContainer.getChildren().clear();

        for (Account account :
                Main.accounts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountscene.fxml"));
            Parent root = loader.load();

            ((AccountSceneController) loader.getController()).setSitename(account.getSitename());
            ((AccountSceneController) loader.getController()).setUsername(account.getUsername());
            ((AccountSceneController) loader.getController()).setPassword(Encryption.decode(account.getPassword()));

            vboxAccountContainer.getChildren().add(root);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Path accountsFile = Paths.get("accounts.data");

        if (Files.exists(accountsFile)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(accountsFile))) {
                Main.accounts = (List<Account>) stream.readObject();
                loadAllAcounts();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
