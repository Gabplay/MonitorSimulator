package com.digitalicagroup.example.monitor;

import com.jfoenix.controls.JFXDecorator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = SingletonObjects.INSTANCE.getRoot();
			
			primaryStage.setResizable(false);
			JFXDecorator decorator = new JFXDecorator(primaryStage, root, true, false, true);
			decorator.setCustomMaximize(true);
			
			Scene scene = new Scene(decorator,810,640);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("icons/monitor.png"));
			primaryStage.show();
			
			root.requestFocus();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
