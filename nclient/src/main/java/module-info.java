module com.vladikusi.ninjaclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vladikusi.ninjaclient to javafx.fxml;
    exports com.vladikusi.ninjaclient;
}
