module com.vladikusi.ninjaserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.vladikusi.ninjaserver to javafx.fxml;
    exports com.vladikusi.ninjaserver;
}
