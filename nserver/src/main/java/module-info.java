module com.vladikusi.ninjaserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive org.json;
    requires transitive json.simple;

    opens com.vladikusi.ninjaserver to javafx.fxml;
    exports com.vladikusi.ninjaserver;
}
