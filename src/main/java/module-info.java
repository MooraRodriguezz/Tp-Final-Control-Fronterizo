module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.controlfrontera.usuarios;
    opens com.controlfrontera.usuarios to javafx.fxml;
    exports com.controlfrontera.modelo;
    opens com.controlfrontera.modelo to javafx.fxml;
}