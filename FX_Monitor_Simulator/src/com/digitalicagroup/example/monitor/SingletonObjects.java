package com.digitalicagroup.example.monitor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public enum SingletonObjects {
	INSTANCE;
	
	private AnchorPane root;
	
	public AnchorPane getRoot() {
		if (root == null) {
			try {
				root = (AnchorPane)FXMLLoader.load(getClass().getResource("monitorFX.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return root;
	}
}
