package com.jonhu.app.controllers;

import com.jonhu.core.builder.document.DocumentBuilder;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class OutputChooseController {
    @FXML
    void onLastAction(MouseEvent event) {
        ControllerManager.turnPanes(ControllerManager.outputChoosePane, ControllerManager.defaultPane);
    }

    @FXML
    void onWordOutputAction(MouseEvent event) {
        ControllerManager.startTask(new DocumentBuilder(), ".docx", ControllerManager.outputChoosePane);
    }

    @FXML
    void onPptxOutputAction(MouseEvent event) {
        ControllerManager.turnPanes(ControllerManager.outputChoosePane, ControllerManager.pptPane);
        //ControllerManager.startTask(new PresentationStaticBuilder(), ".pptx", ControllerManager.outputChoosePane);
    }

    @FXML
    void onMdOutputAction(MouseEvent event) {

    }
}
