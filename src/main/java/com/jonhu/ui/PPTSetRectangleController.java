package com.jonhu.ui;

import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class PPTSetRectangleController {

    private static final double SLIDE_WIDTH = 720;

    private static final double SLIDE_HEIGHT = 540;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField xTextField;

    @FXML
    private TextField yTextField;

    @FXML
    private Rectangle textFrameRectangle;

    @FXML
    private Rectangle backgroundRectangle;

    @FXML
    void initialize() {
        java.awt.Rectangle rectangle = Settings.textFrameRectangles.get(Constant.TEMP_STRING);
        setRectangle(rectangle);
        //初始化输入回车监听
        initFields();
    }

    private void initFields() {
        EventHandler<KeyEvent> handler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                PPTSetRectangleController.this.showEffect();
            }
        };
        widthTextField.setOnKeyReleased(handler);
        heightTextField.setOnKeyReleased(handler);
        xTextField.setOnKeyReleased(handler);
        yTextField.setOnKeyReleased(handler);
    }

    @FXML
    void setOnShowEffectAction(MouseEvent event) {
        showEffect();
    }

    private void showEffect() {
        try {
            double height = Double.parseDouble(heightTextField.getText());
            double width = Double.parseDouble(widthTextField.getText());
            double x = Double.parseDouble(xTextField.getText());
            double y = Double.parseDouble(yTextField.getText());
            Settings.textFrameRectangles.replace(Constant.TEMP_STRING, new java.awt.Rectangle((int) x, (int) y, (int) width, (int) height));
            setRectangle(Settings.textFrameRectangles.get(Constant.TEMP_STRING));
        } catch (NumberFormatException e) {
            Utils.alert("提示","您输入的值包含非法字符,请重新输入");
        }

    }

    private void setRectangle(java.awt.Rectangle rectangle) {
        textFrameRectangle.setWidth(backgroundRectangle.getWidth()*rectangle.width/SLIDE_WIDTH);
        textFrameRectangle.setHeight(backgroundRectangle.getHeight()*rectangle.height/SLIDE_HEIGHT);
        textFrameRectangle.setTranslateX((rectangle.x*25)/36);
        textFrameRectangle.setTranslateY((rectangle.y*5)/6);
        heightTextField.setText(String.valueOf(rectangle.getHeight()));
        widthTextField.setText(String.valueOf(rectangle.getWidth()));
        xTextField.setText(String.valueOf(rectangle.getX()));
        yTextField.setText(String.valueOf(rectangle.getY()));
    }



}
