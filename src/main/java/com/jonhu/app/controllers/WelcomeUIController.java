package com.jonhu.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class WelcomeUIController {
    @FXML
    void OnEnterAction(MouseEvent event) {
        ControllerManager.turnPanes(ControllerManager.welcomePane, ControllerManager.defaultPane);
    }

    /**
     * 两个点击彩蛋
     */
    @FXML
    void onIconClickAction(MouseEvent event) {

    }

    @FXML
    void onNameClickAction(MouseEvent event) {

    }
}
