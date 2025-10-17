package com.controlfrontera.Controllers;

import com.controlfrontera.modelo.GestorPaises;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ReglamentoViewController {

    @FXML
    private TextArea reglamentoTextArea;

    private final DateTimeFormatter formatterFechaHoy = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        cargarReglamento();
    }

    private void cargarReglamento() {
        GestorPaises gestorPaises = GestorPaises.getInstancia();
        String paisesValidosStr = gestorPaises.getPaisesValidos().stream()
                .collect(Collectors.joining(", "));

        StringBuilder contenido = new StringBuilder();
        contenido.append("REGLAS VIGENTES - ").append(LocalDate.now().format(formatterFechaHoy)).append("\n");
        contenido.append("=========================================\n\n");
        contenido.append("** PAÍSES ADMITIDOS **\n");
        contenido.append(paisesValidosStr).append("\n\n");
        contenido.append("** DOCUMENTACIÓN REQUERIDA **\n");
        contenido.append("- PASAPORTE:\n");
        contenido.append("    - Debe estar vigente.\n");
        contenido.append("- PERMISO DE ENTRADA:\n");
        contenido.append("    - Debe estar vigente.\n");
        contenido.append("    - Motivo: Turismo, Negocios o Tránsito.\n");
        contenido.append("- PERMISO DE TRABAJO:\n");
        contenido.append("    - Debe estar vigente.\n");
        contenido.append("    - Profesión: INGENIERO, MEDICO, DIPLOMATICO, TECNICO ESPECIALIZADO.\n\n");
        contenido.append("** CONTROLES ADICIONALES **\n");
        contenido.append("- PESO:\n");
        contenido.append("    - Discrepancias > 3.0 kg activan Rayos-X.\n");
        contenido.append("- RAYOS-X:\n");
        contenido.append("    - Si detecta contrabando, se habilita ARRESTO.\n");
        contenido.append("- DISCREPANCIAS:\n");
        contenido.append("    - Comparar datos entre documentos y persona.\n\n");
        contenido.append("** ARRESTO **\n");
        contenido.append("- Obligatorio si Rayos-X detecta contrabando.\n");
        contenido.append("- Arrestar a un inocente conlleva penalización severa.\n");

        reglamentoTextArea.setText(contenido.toString());
    }
}