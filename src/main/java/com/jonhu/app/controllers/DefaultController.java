package com.jonhu.app.controllers;

import com.jonhu.util.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class DefaultController {
    @FXML
    private TextArea srcPathsInput;

    @FXML
    private TextField destPathInput;

    @FXML
    void onNextAction(MouseEvent event) {
        ControllerManager.turnPanes(ControllerManager.defaultPane, ControllerManager.outputChoosePane);
        setSrcFiles();
        setDestDir();
    }

    @FXML
    void onChooseFileFromComputerAction(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        List<File> src = chooser.showOpenMultipleDialog(stage);

        if (ControllerManager.srcFiles == null || src == null) {
            return;
        }

        setSrcFiles();
        ControllerManager.srcFiles.addAll(src);

        StringBuilder builder = new StringBuilder();
        ControllerManager.srcFiles.forEach((file)-> builder.append(file.getAbsolutePath()).append("\n"));
        srcPathsInput.setText(builder.toString());
    }

    @FXML
    void onChooseDirFromComputerAction(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        ControllerManager.destDir = chooser.showDialog(stage);

        if (ControllerManager.destDir == null) {
            return;
        }

        destPathInput.setText(ControllerManager.destDir.getAbsolutePath());
    }

    private void setSrcFiles() {
        ControllerManager.srcFiles = new HashSet<>();
        String[] paths = srcPathsInput.getText().split("\n");
        for(String path : paths) {
            ControllerManager.srcFiles.add(new File(path));
        }
    }

    private void setDestDir() {
        ControllerManager.destDir = new File(destPathInput.getText());
    }

}
