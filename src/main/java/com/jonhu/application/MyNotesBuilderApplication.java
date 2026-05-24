package com.jonhu.application;

import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

@Deprecated
public class MyNotesBuilderApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        TODO:
            1. MenuBar监听事件
            2. PPTSetAnimationController#initVideoPane()会出现异常
            3. parser.styles.Style是枚举,不够灵活,应当改成Map
         */
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/old/main-ui.fxml")));
        primaryStage.setScene(new Scene(root,null));
        primaryStage.setTitle("MyNotesBuilder - Jon Hu");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Settings.defaultInitialize();
        launch(args);
    }
}
