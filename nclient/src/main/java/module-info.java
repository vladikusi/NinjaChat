module com.vladikusi.ninjaclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive org.json;

    opens com.vladikusi.ninjaclient to javafx.fxml;
    exports com.vladikusi.ninjaclient;
}
