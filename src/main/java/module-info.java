module com.prepper {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires java.desktop;
    requires eu.hansolo.tilesfx;
    requires eu.hansolo.fx.charts;
    opens com.prepper to javafx.fxml;
    exports com.prepper;
}
