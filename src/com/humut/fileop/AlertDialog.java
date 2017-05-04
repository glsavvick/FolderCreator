package com.humut.fileop;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class AlertDialog {

	public static Optional<ButtonType> alertBox(String alertMessage, String titleBar, AlertType type){
		return alertBox(alertMessage, titleBar, null, type);
	}

	public static Optional<ButtonType> alertBox(String alertMessage, String titleBar, String headerMessage, AlertType type) {
		Alert alert = new Alert(type);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(alertMessage);
        return alert.showAndWait();
	}
	
	public static Optional<String> textInputBox(String title, String content){
		return textInputBox(title, null, content);
	}
	
	public static Optional<String> textInputBox(String title, String header, String content){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();

		if(result.isPresent())
			return result;
		return null;
	}
	
}
