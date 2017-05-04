package com.humut.fileop;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public final class Main extends Application {

	private int fileNumber = 0;
	private String dirName = null;
	private String dirPath = null;
	private final String chooseFolder = " Choose Folder ";
	private final String tooltipChooseFolder = "Choose a folder..";
	private final String iDidWrong = "  I did something wrong. Please god make it go away! ";
	private static int undoCount = 0;
	public static Scene mainScene = null;

	@Override
	public void start(final Stage stage) {
		stage.setTitle("Folder Creator");
		stage.getIcons().add(new Image("file:resource\\icon2.png"));
		stage.setResizable(false);

		final DirectoryChooser dirChooser = new DirectoryChooser();

		final Label folderName = new Label("Name: ");
		final Label folderNum = new Label("Folder #: ");
		final TextField textName = new TextField("...");
		final TextField textField = new TextField("...");
		final Button openButton = new Button(chooseFolder);
		final Button openMultipleButton = new Button("Create Folder(s)");
		final Button undoButton = new Button(iDidWrong + " " + undoCount);
		final Button arbitraryButton = new Button("                Look for the selected directory path                 ");
		
		final GridPane inputGridPane = new GridPane();

		GridPane.setConstraints(folderNum, 0, 0);
		GridPane.setConstraints(textField, 1, 0);
		GridPane.setConstraints(folderName, 0, 2);
		GridPane.setConstraints(textName, 1, 2);
		GridPane.setConstraints(openButton, 2, 0);
		GridPane.setConstraints(openMultipleButton, 2, 2);
		GridPane.setConstraints(undoButton, 0, 4, GridPane.REMAINING, 2);
		GridPane.setConstraints(arbitraryButton, 0, 6, GridPane.REMAINING, 2);
		inputGridPane.setHgap(6);
		inputGridPane.setVgap(6);
		inputGridPane.getChildren().addAll(folderNum, textField, folderName, textName, openButton, openMultipleButton,
				undoButton, arbitraryButton);

		final Pane rootGroup = new VBox(12);
		rootGroup.getChildren().addAll(inputGridPane);
		rootGroup.setPadding(new Insets(12, 12, 12, 12));
		Scene firstScene = new Scene(rootGroup);
		Main.mainScene = firstScene;
		
		stage.setScene(firstScene);
		stage.show();

		openButton.setTooltip(new Tooltip(tooltipChooseFolder));
		openButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {
				
				while (true) {
					try {
						File dir = dirChooser.showDialog(stage);
						if (dir.exists()) {
							dirPath = dir.getAbsolutePath();
							openButton.setText(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1));
							openButton.setTooltip(new Tooltip(dirPath));
							break;
						}
					} 
					catch (Exception exc) {
						AlertDialog.alertBox("Please choose a folder!", "Warning", AlertType.WARNING);
					}
				}
			}
		});

		openMultipleButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {
				
				if (dirPath != null && dirName != null && fileNumber != 0) {
					boolean check = false;
					for (int i = 0; i < fileNumber; i++) {
						check = createFile(i);
					}
					if (check)
					{
						undoButton.setText(iDidWrong + " " + ++undoCount);
						if(undoCount == 0)
							undoButton.setDisable(true);
						else
							undoButton.setDisable(false);
						UndoOperation.undoList.add(new UndoOperation(fileNumber, dirPath, dirName));
						AlertDialog.alertBox("Folders created", "Confirm", AlertType.INFORMATION);
						
					}
						
				} 
				else
					AlertDialog.alertBox("Somethings wrong!! Please check if you choose proper path.", "Error",
							AlertType.ERROR);

			}
		});

		if(undoCount == 0)
			undoButton.setDisable(true);
		else
			undoButton.setDisable(false);
		undoButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {
				boolean result = false;
				result = UndoOperation.undo();
				if (result)
				{
					undoButton.setText(iDidWrong + " " + --undoCount);
					if(undoCount == 0)
						undoButton.setDisable(true);
					else
						undoButton.setDisable(false);
					AlertDialog.alertBox("Okay son, i'm God. I did handle the situation.", "Confirm",
							AlertType.INFORMATION);
				}
				else
					AlertDialog.alertBox("Son, i'm sorry. There was an error during the process", "Error",
							AlertType.ERROR);
				
				dirPath = null;
				openButton.setText(chooseFolder);
				openButton.setTooltip(new Tooltip(tooltipChooseFolder));
			}
		});
		
		arbitraryButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(final ActionEvent e)
			{
				if(dirPath != null)
				{
					DirectoryViewer dirView = new DirectoryViewer(dirPath);
					dirView.start(stage);
				}
				else
				{
					AlertDialog.alertBox("Sorry no path had chosen", "Error", AlertType.ERROR);
				}
				
			}
		});

		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				try {
					fileNumber = Integer.parseInt(textField.getText());

				} 
				catch (Exception e) {
					AlertDialog.alertBox("Please enter integer value!", "Warning", AlertType.WARNING);
					textField.deletePreviousChar();
				}

			}
		});

		textName.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) 
			{

				try {
					dirName = textName.getText();
				} 
				catch (Exception e) {
					AlertDialog.alertBox("Please enter valid value!", "Warning", AlertType.WARNING);
					textName.deletePreviousChar();
				}

			}
		});
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public static void setStageHere()
	{
		
	}

	private boolean createFile(int i) 
	{
		try 
		{
			File tempFile = new File(dirPath + File.separator + dirName + " " + ++i);

			if (tempFile.exists()) 
			{

				Optional<ButtonType> result = AlertDialog.alertBox(dirName + i + " already exist! Give another name?",
						"Confirm", AlertType.CONFIRMATION);

				if (result.get() == ButtonType.OK) 
				{
					Optional<String> optStr = AlertDialog.textInputBox("Name", "Give another name: ");
					if (optStr != null) 
					{
						File f = new File(dirPath + File.separator + optStr.get());
						f.mkdir();
						return true;
					}
					AlertDialog.alertBox("You haven't entered any name!", "Error", AlertType.ERROR);
					return false;

				} 
				else 
				{
					System.out.println("CANCEL");
					return false;
				}
			} 
			else 
			{
				tempFile.mkdir();
				return true;
			}

		} 
		catch (Exception e) 
		{
			return false;
		}
	}

}
