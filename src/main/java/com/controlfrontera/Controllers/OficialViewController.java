package com.controlfrontera.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
// import javafx.scene.image.Image;

public class OficialViewController {

    @FXML
    private Button aprobarButton;
    @FXML
    private Button rechazarButton;
    @FXML
    private TextArea documentosTextArea;
    @FXML
    private ImageView fotoPersonaImageView;
    @FXML
    private Label motivoLabel;
    @FXML
    private Label nacionalidadLabel;
    @FXML
    private Label nombreLabel;
    @FXML
    private Label tituloPrincipalLabel;

    @FXML
    public void initalize(){
        //cargarSiguientePersona();//
    }

    @FXML
    void onAprobarClick(ActionEvent event){
        System.out.println("Decision: aprobado");
        //cargarSiguientePersona();//
    }
    @FXML
    void onRechazarClick(ActionEvent event){
        System.out.println("Decision: rechazada");
        //cargarSiguientePersona();//
    }

    /*private void cargarSiguientePersona(){}*/

}
