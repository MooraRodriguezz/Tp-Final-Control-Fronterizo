package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.Documento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;

public class DocumentoDetalleViewController {

    @FXML private Label lblTipo;
    @FXML private Label lblNumero;
    @FXML private Label lblPaisEmisor;
    @FXML private Label lblFechaExp;
    @FXML private Label lblEstado;

    /**
     * Este metodo es llamado desde el OficialViewController para pasarle el documento seleccionado.
     */
    public void initData(Documento documento) {
        lblTipo.setText(documento.getTipo());
        lblNumero.setText(documento.getNumeroIdentificacion());
        lblPaisEmisor.setText(documento.getPaisEmisor());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        lblFechaExp.setText(formatter.format(documento.getFechaExpiracion()));
    }
}