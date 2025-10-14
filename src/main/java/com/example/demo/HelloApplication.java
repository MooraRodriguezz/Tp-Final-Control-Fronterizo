package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        try (InputStream fontStream = HelloApplication.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf")) {
            if (fontStream != null) {
                Font.loadFont(fontStream, 12);
                System.out.println(">>> FUENTE 'Press Start 2P' CARGADA CORRECTAMENTE EN JAVA.");
            } else {
                System.err.println(">>> ERROR CRÍTICO: No se encontró el archivo '/fonts/PressStart2P-Regular.ttf'");
            }
        } catch (Exception e) {
            System.err.println(">>> ERROR FATAL AL CARGAR LA FUENTE:");
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 350);
        stage.setTitle("Control Fronterizo - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}