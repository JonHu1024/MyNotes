package com.jonhu.app.controllers;

import com.spire.presentation.drawing.animation.AnimationEffectType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * @author Jon Hu
 */
public class PptOutputController {

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	void initialize() {
		AnimationEffectType[] values = AnimationEffectType.values();
		ObservableList<String> items = FXCollections.observableArrayList();
		for(AnimationEffectType value: values) {
			items.add(String.valueOf(value));
		}
		comboBox.setItems(items);
	}

}
