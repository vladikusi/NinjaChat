module com.vladikusi.ninjaserver {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vladikusi.ninjaserver to javafx.fxml;
    exports com.vladikusi.ninjaserver;
}
