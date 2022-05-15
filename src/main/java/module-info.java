module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.ow2.sat4j.core;
    requires transitive javafx.graphics;

    opens com.example to javafx.fxml;
    exports com.example;
}
