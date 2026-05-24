package com.jonhu.app;

import com.jonhu.app.controllers.ControllerManager;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * TODO:
    1. 多个文件同时转化有问题,但是单个文件的转换没有问题
 */

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadPanes(primaryStage);
        setIcon(primaryStage);
        setStage(primaryStage);
    }

    private void loadPanes(Stage primaryStage) throws IOException {
        //初始化界面
        initPanes();

        StackPane root = new StackPane(ControllerManager.welcomePane, ControllerManager.defaultPane, ControllerManager.outputChoosePane, /*ControllerManager.wordPane,*/ ControllerManager.pptPane/*, ControllerManager.mdPane*/);

        primaryStage.setScene(new Scene(root));
    }

    private void initPanes() throws IOException {
        ControllerManager.welcomePane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/welcome.fxml")));

        ControllerManager.defaultPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/default.fxml")));
        ControllerManager.defaultPane.setVisible(false);

        ControllerManager.outputChoosePane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/outputChoose.fxml")));
        ControllerManager.outputChoosePane.setVisible(false);

        //ControllerManager.wordPane = FXMLLoader.load(getClass().getResource("/fxml/customize/word.fxml"));
        //ControllerManager.wordPane.setVisible(false);

        ControllerManager.pptPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/customize/ppt.fxml")));
        ControllerManager.pptPane.setVisible(false);

        //ControllerManager.mdPane = FXMLLoader.load(getClass().getResource("/fxml/customize/md.fxml"));
        //ControllerManager.mdPane.setVisible(false);
    }

    private void setStage(Stage primaryStage) {
        primaryStage.setHeight(Constant.WINDOW_HEIGHT);
        primaryStage.setWidth(Constant.WINDOW_WIDTH);
        primaryStage.setResizable(false);
        primaryStage.setTitle("MyNotesBuilder - JonHu");
        primaryStage.show();
    }

    private void setIcon(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("img/MyNotesBuilderIcon.png"));
    }

    public static void main(String[] args) {
        Settings.defaultInitialize();
        launch(args);
    }
}
