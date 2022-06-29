package passlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private TitledPane tpAddNewAcc;
    @FXML
    private Accordion accordionNewAccount;
    @FXML
    private Button btnGenerate;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnRefresh;
    @FXML
    private ScrollPane spAccountsListContainer;
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

        for (Account account :
                Main.masterAccount.getAccounts()) {
            String sitename = account.getSitename().toLowerCase();
            String username = account.getUsername().toLowerCase();

            boolean isSitenameSubstr = false;
            boolean isUsernameSubstr = false;

            if (sitename.length() >= toFind.length())
                isSitenameSubstr = toFind.equals(sitename.substring(0, toFind.length()));
            if (username.length() >= toFind.length())
                isUsernameSubstr = toFind.equals(username.substring(0, toFind.length()));

            if (isSitenameSubstr || isUsernameSubstr) {
                System.out.println("Account substring");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("accountScene.fxml"));
                Parent root = loader.load();

                ((AccountSceneController) loader.getController()).setSitename(account.getSitename());
                ((AccountSceneController) loader.getController()).setUsername(account.getUsername());
                ((AccountSceneController) loader.getController()).setPassword(Encryption.decode(account.getPassword()));

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
        Main.masterAccount.getAccounts().add(newAccount);

        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("psslck.data")))) {
            stream.writeObject(Main.masterAccount);
            System.out.println("New account saved!");
            System.out.println("Sitename: " + newAccount.getSitename() + ", Username: " + newAccount.getUsername() + ", Encrypted password: " + newAccount.getPassword());
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
        lblErrorMsg.setVisible(false);
    }

    public void refresh() throws IOException {
        loadAllAcounts();
        lblErrorMsg.setVisible(false);
    }

    public void clear() throws IOException {
        tfSearch.clear();
        loadAllAcounts();
    }

    public void loadAllAcounts() throws IOException {
        vboxAccountContainer.getChildren().clear();

        for (Account account :
                Main.masterAccount.getAccounts()) {
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
        Path accountsFile = Paths.get("psslck.data");

        if (Files.exists(accountsFile)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(accountsFile))) {
                Main.masterAccount = (MasterAccount) stream.readObject();
                loadAllAcounts();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        spAccountsListContainer.setFitToWidth(true);

        Tooltip togglePassTooltip = new Tooltip();
        togglePassTooltip.setText("show/hide password");
        togglePassTooltip.setShowDelay(Duration.millis(200));
        cbTogglePassword.setTooltip(togglePassTooltip);

        Tooltip searchTT = new Tooltip();
        searchTT.setText("search");
        searchTT.setShowDelay(Duration.millis(200));
        btnSearch.setTooltip(searchTT);

        Tooltip clearTT = new Tooltip();
        clearTT.setText("clear search");
        clearTT.setShowDelay(Duration.millis(200));
        btnClear.setTooltip(clearTT);

        Tooltip refreshTT = new Tooltip();
        refreshTT.setText("refresh");
        refreshTT.setShowDelay(Duration.millis(200));
        btnRefresh.setTooltip(refreshTT);

        Tooltip generateTT = new Tooltip();
        generateTT.setText("generate password");
        generateTT.setShowDelay(Duration.millis(200));
        btnGenerate.setTooltip(generateTT);
    }

    public void focusSearch(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.S) {
            tfSearch.requestFocus();
        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.W) {
            Platform.exit();
        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.N) {
            openAddNewAccAccordion();
        }

        if (keyEvent.getCode() == KeyCode.ESCAPE && accordionNewAccount.getExpandedPane() != null) {
            accordionNewAccount.getExpandedPane().setExpanded(false);
        }
    }

    public void openAddNewAccAccordion() {
        accordionNewAccount.setExpandedPane(tpAddNewAcc);
        tfSitename.requestFocus();
    }

    public void showAboutScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader aboutSceneLoader = new FXMLLoader(getClass().getResource("aboutScene.fxml"));
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(aboutSceneLoader.load()));
        aboutStage.setTitle("About Passlock");
        aboutStage.getIcons().add(new Image(getClass().getResourceAsStream("Passlock.png")));
        aboutStage.show();
    }
}
