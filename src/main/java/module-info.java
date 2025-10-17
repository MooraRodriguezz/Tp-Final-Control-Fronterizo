module com.example.demo {
    // --- LIBRERÍAS REQUERIDAS ---
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson; // Requerido para la persistencia JSON

    // --- PERMISOS (OPENS) ---

    // Permisos para que JavaFX acceda a los controladores y modelos
    opens com.controlfrontera.Controllers to javafx.fxml;
    opens com.controlfrontera.modelo to javafx.fxml;
    opens com.controlfrontera.persistencia to javafx.fxml;

    // ¡AQUÍ ESTÁ LA CORRECCIÓN!
    // Se elimina el duplicado y se añade el permiso para 'com.google.gson'
    opens com.controlfrontera.usuarios to javafx.fxml, com.google.gson;

    // --- PAQUETES EXPORTADOS ---
    exports com.example.demo;
    exports com.controlfrontera.usuarios;
    exports com.controlfrontera.modelo;
    exports com.controlfrontera.persistencia;
}