package com.humut.fileop;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DirectoryViewer extends Application{
	
	private String dirPath;
	
	public DirectoryViewer(String dirPath){
		this.dirPath = dirPath;
	}

	@Override
    public void start(Stage primaryStage) {
		
        TreeView<String> a = new TreeView<String>();
        BorderPane b = new BorderPane();
        Button c = new Button("Back");
        
        c.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e)
        	{
        		if(Main.mainScene != null)
        		{
        			primaryStage.setScene(Main.mainScene);
        			primaryStage.setTitle("Folder Creator");        			
        		}
        	}
		});
        
        File choice = new File(dirPath);
        a.setRoot(getNodesForDirectory(choice));
        
        b.setTop(c);
        b.setCenter(a);
        primaryStage.setScene(new Scene(b, 400, 300));
        primaryStage.setTitle("Folder View");
        primaryStage.show();
    }
	
	public TreeItem<String> getNodesForDirectory(File directory) {
		
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        
        for(File f : directory.listFiles()) {
            System.out.println("Loading " + f.getName());
            
            if(f.isDirectory())             
                root.getChildren().add(getNodesForDirectory(f));            
            else             
                root.getChildren().add(new TreeItem<String>(f.getName()));
            
        }
        return root;
    }
	
}
