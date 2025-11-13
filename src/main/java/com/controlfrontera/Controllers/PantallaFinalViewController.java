package com.controlfrontera.Controllers;

import com.controlfrontera.usuarios.GestorSonido;
import com.controlfrontera.usuarios.Oficial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaFinalViewController {

    @FXML private Label lblTotal;
    @FXML private Label lblAciertos;
    @FXML private Label lblErrores;
    @FXML private Label lblPuntuacion;
    @FXML private Button btnVolverMenu;
    @FXML private Button btnCerrarSesion;

    private Oficial oficialLogueado;

    public void initData(Oficial oficial) {
        this.oficialLogueado = oficial;
        actualizarStats();
    }

    private void actualizarStats() {
        if (oficialLogueado != null) {
            int total = oficialLogueado.getTotalAciertos() + oficialLogueado.getTotalErrores();
            lblTotal.setText(String.valueOf(total));
            lblAciertos.setText(String.valueOf(oficialLogueado.getTotalAciertos()));
            lblErrores.setText(String.valueOf(oficialLogueado.getTotalErrores()));
            lblPuntuacion.setText(String.valueOf(oficialLogueado.getPuntuacion()));
        }
    }


    @FXML
    void onVolverMenuClick(ActionEvent event) {
        GestorSonido.reproducirMenuClick();
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/pantalla-inicio-oficial.fxml"));
            Parent oficialStartView = loader.load();


            PantallaInicioOficialController controller = loader.getController();
            controller.initData(oficialLogueado);

            Scene startScene = new Scene(oficialStartView, 800, 600);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(startScene);
            window.setTitle("Bienvenido Oficial");
            window.centerOnScreen();
            window.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de inicio del oficial:");
            e.printStackTrace();
        }
    }


    @FXML
    void onCerrarSesionClick(ActionEvent event) {
        GestorSonido.reproducirMenuClick();
        try {

            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/demo/login-view.fxml"));
            Scene loginScene = new Scene(loginView, 550, 400);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(loginScene);
            window.setTitle("Control Fronterizo - Login");
            window.centerOnScreen();
            window.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de login:");
            e.printStackTrace();
        }
    }
}