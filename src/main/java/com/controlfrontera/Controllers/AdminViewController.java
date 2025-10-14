package com.controlfrontera.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminViewController {
    @FXML
    private Button cerrarSesionButton;

    @FXML
    private Button configurarPaisesButton;

    @FXML
    private Button gestionarUsuariosButton;

    @FXML
    private Label tituloPrincipal;

    @FXML
    private Button verEstadisticasButton;
    //Metodos de prueba pq no hay codigo DJjdasj
    @FXML
    void onCerrarSesionClick(ActionEvent event){
        System.out.println("ACCION: Cerrar Sesion");
        //Obviamente aca va la logica para volver a la pantalla del login
    }
    @FXML
    void onConfigurarPaisesClick(ActionEvent event){
        System.out.println("ACCION: COnfigurar pasies");
    }
    @FXML
    void onGestionarUsuariosClick(ActionEvent event){
        System.out.println("ACCION: Gestionar Usuarios");
    }
    @FXML
    void onVerEstadisticasClick(ActionEvent event){
        System.out.println("ACCION: Ver estadisticas");
    }




}
