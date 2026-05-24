package com.jonhu.ui;

import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.spire.presentation.drawing.animation.AnimationEffectType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PPTSetAnimationController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<String> animationTypeList;

    private ContextMenu contextMenu;

    @FXML
    void initialize() {
        try {
            initAnimationTypeList();
            //initVideoPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAnimationTypeList() {
        AnimationEffectType[] values = AnimationEffectType.values();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (AnimationEffectType value : values) {
            items.add(value.getName());
        }
        animationTypeList.setItems(items);
    }

/*    private void initVideoPane() throws MalformedURLException {
        //TODO 有问题
        player = new MediaPlayer(new Media(Paths.get("src/main/resources/video/EffectsShow.mp4").toUri().toURL().toString()));

        view = new MediaView(player);
        view.setFitHeight(700);
        view.setFitWidth(1024);

        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(view);

        contextMenu = new ContextMenu(new SeparatorMenuItem());
        contextMenu.getScene().setRoot(pane);
    }*/

    @FXML
    void setOnShowVideoAction(MouseEvent event) {
        contextMenu.show(anchorPane, 0 ,0);
        //player.play();
    }

    @FXML
    void setOnConfirmAction(MouseEvent event) {

        Settings.animationEffectTypes.replace(Constant.TEMP_STRING, AnimationEffectType.valueOf(animationTypeList.getSelectionModel().getSelectedItem()));
    }

    //

}