module com.vladikusi.ninjaclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens com.vladikusi.ninjaclient to javafx.fxml;
    exports com.vladikusi.ninjaclient;
}
