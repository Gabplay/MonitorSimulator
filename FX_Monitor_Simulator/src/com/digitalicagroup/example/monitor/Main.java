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
			
			
			JFXDecorator decorator = new JFXDecorator(primaryStage, root, true, true, true);
			decorator.setCustomMaximize(true);
			
			Scene scene = new Scene(decorator,810,740);
			primaryStage.setResizable(true);
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(700);
			
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
