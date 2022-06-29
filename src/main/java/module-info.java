module passlock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;


    opens passlock to javafx.fxml;
    exports passlock;
}