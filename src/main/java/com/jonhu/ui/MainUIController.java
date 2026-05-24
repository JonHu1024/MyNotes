package com.jonhu.ui;

import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.document.DocumentBuilder;
import com.jonhu.core.builder.presentation.AnimatedPresentationBuilder;
import com.jonhu.core.builder.presentation.StaticPresentationBuilder;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.util.Constant;
import com.jonhu.util.Utils;
import com.leewyatt.rxcontrols.controls.RXTranslationButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainUIController {

    private List<File> srcFiles = new ArrayList<>();
    private File destDir;
    private Timeline docShowAnimation;
    private Timeline pptShowAnimation;
    private Timeline mdShowAnimation;
    private Timeline docHideAnimation;
    private Timeline pptHideAnimation;
    private Timeline mdHideAnimation;

    //PPT文本框布局弹窗
    private ContextMenu contextMenu;

    private Parent setRectangleRoot;

    private Parent setAnimationRoot;

    @FXML
    private RXTranslationButton docOutputButton;

    @FXML
    private RXTranslationButton pptOutputButton;

    @FXML
    private RXTranslationButton mdOutputButton;

    @FXML
    private Pane docPane;

    @FXML
    private Pane pptPane;

    @FXML
    private Pane mdPane;

    @FXML
    private Label outputTypeInfo;

    @FXML
    private TextArea srcFilesTextArea;

    @FXML
    private TextField docDestFileTextField;

    @FXML
    private TextField pptDestFileTextField;

    @FXML
    private TextField mdDestFileTextField;

    @FXML
    private CheckBox pptAnimationCheckBox;

    @FXML
    void initialize() {
        initAnimation();
        initContextMenu();
    }

    private void initContextMenu() {
        try {
            contextMenu = new ContextMenu(new SeparatorMenuItem());
            setRectangleRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/old/ppt-setRectangle-ui.fxml")));
            setAnimationRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/old/ppt-setAnimation-ui.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAnimation() {
        docShowAnimation = initTimeline(docPane, 0);
        docHideAnimation = initTimeline(docPane, 200);
        pptShowAnimation = initTimeline(pptPane, -200);
        pptHideAnimation = initTimeline(pptPane, 0);
        mdShowAnimation = initTimeline(mdPane, -400);
        mdHideAnimation = initTimeline(mdPane, -200);

        docHideAnimation.setOnFinished(event -> docPane.setVisible(false));
        pptHideAnimation.setOnFinished(event -> pptPane.setVisible(false));
        mdHideAnimation.setOnFinished(event -> mdPane.setVisible(false));
    }

    @FXML
    void setOnDocOutputAction(MouseEvent event) {
        hideShowingPane();
        outputTypeInfo.setText(Constant.DOC_TYPE_INFO);
        docPane.setVisible(true);
        docShowAnimation.play();
    }

    @FXML
    void setOnMdOutputAction(MouseEvent event) {
        hideShowingPane();
        outputTypeInfo.setText(Constant.MD_TYPE_INFO);
        mdPane.setVisible(true);
        mdShowAnimation.play();
    }

    @FXML
    void setOnPptOutputAction(MouseEvent event) {
        hideShowingPane();
        outputTypeInfo.setText(Constant.PPT_TYPE_INFO);
        pptPane.setVisible(true);
        pptShowAnimation.play();
    }

    @FXML
    void setOnChooseFromFileAction(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文件类型", Constant.EXTENSIONS));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        List<File> list = chooser.showOpenMultipleDialog(stage);
        if (list==null) {
            return;
        }
        srcFiles.removeAll(list);
        srcFiles.addAll(list);
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = srcFiles.size(); i < size; i++) {
            builder.append(srcFiles.get(i).getAbsolutePath() + "\n");
        }
        srcFilesTextArea.setText(builder.toString());
    }

    @FXML
    void setOnChooseFromDirectoryAction(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        destDir = chooser.showDialog(stage);
        if (destDir == null) {
            return;
        }
        if (docPane.isVisible()) {
            docDestFileTextField.setText(destDir.getAbsolutePath());
        } else if (pptPane.isVisible()) {
            pptDestFileTextField.setText(destDir.getAbsolutePath());
        } else if (mdPane.isVisible()) {
            mdDestFileTextField.setText(destDir.getAbsolutePath());
        }
    }

    @FXML
    void setOnDocStartToExchangeAction(MouseEvent event) {
        setSrcFiles();
        setDestDir();
        srcFiles.forEach(srcFile -> {
            File destFile = getDestFile(srcFile, ".docx");
            try {
                //显示转换任务
                outputTypeInfo.setText("正在转换中,请稍候...");
                new ConvertingTask(srcFile, destFile, new DocumentBuilder(), new MyNotesFormatParser()).start();
                //提示操作已经完成
                outputTypeInfo.setText("");
                Utils.alert("转换已完成!", "您的文件已经输出到了\n" + destFile.getAbsolutePath() + "\n目录中");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void setOnPptStartToExchangeAction(MouseEvent event) {
        Builder builder;
        if (pptAnimationCheckBox.isSelected()) {
            builder = new AnimatedPresentationBuilder();
        } else {
            builder = new StaticPresentationBuilder();
        }
        setSrcFiles();
        setDestDir();
        srcFiles.forEach(srcFile -> {
            File destFile = getDestFile(srcFile, ".pptx");
            try {
                //显示转换任务
                outputTypeInfo.setText("正在转换中,请稍候...");
                new ConvertingTask(srcFile, destFile, builder, new MyNotesFormatParser()).start();
                //提示操作已经完成
                outputTypeInfo.setText("");
                Utils.alert("转换已完成!", "您的文件已经输出到了\n" + destFile.getAbsolutePath() + "\n目录中");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void setOnSetRectangleAction(MouseEvent event) {
        contextMenu.getScene().setRoot(setRectangleRoot);
        contextMenu.show(pptPane, event.getScreenX(), event.getScreenY());
    }

    @FXML
    void setOnSetAnimationAction(MouseEvent event) {
        contextMenu.getScene().setRoot(setAnimationRoot);
        contextMenu.show(pptPane, event.getScreenX(), event.getScreenY());
    }

    private Timeline initTimeline(Pane pane, int endValue) {
        return new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(pane.translateYProperty(), endValue)));
    }

    private void hideShowingPane() {
        if (docPane.isVisible()) {
            docHideAnimation.play();
            docPane.setVisible(false);
        } else if (pptPane.isVisible()) {
            pptHideAnimation.play();
            pptPane.setVisible(false);
        } else if (mdPane.isVisible()) {
            mdHideAnimation.play();
            mdPane.setVisible(false);
        }
    }

    private File getDestFile(File src, String extension) {
        return new File(destDir.getAbsolutePath() + File.separator + src.getName().split("\\.")[0] + extension);
    }

    private void setSrcFiles() {
        srcFiles.clear();
        String[] text = srcFilesTextArea.getText().split("\n");
        for (int i = 0; i < text.length; i++) {
            srcFiles.add(new File(text[i]));
        }
    }

    public void setDestDir() {
        if (docPane.isVisible()) {
            destDir = new File(docDestFileTextField.getText());
        } else if (pptPane.isVisible()) {
            destDir = new File(pptDestFileTextField.getText());
        } else if (mdPane.isVisible()) {
            destDir = new File(mdDestFileTextField.getText());
        }
    }

}