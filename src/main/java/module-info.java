module com.example.demo {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;
    requires javafx.media;


    opens com.controlfrontera.Controllers to javafx.fxml;
    opens com.controlfrontera.modelo to javafx.fxml, com.google.gson;
    opens com.controlfrontera.persistencia to javafx.fxml;

    opens com.controlfrontera.usuarios to javafx.fxml, com.google.gson;

    exports com.example.demo;
    exports com.controlfrontera.usuarios;
    exports com.controlfrontera.modelo;
    exports com.controlfrontera.persistencia;
}