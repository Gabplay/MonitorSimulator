package com.digitalicagroup.example.monitor;

import java.util.Random;

import com.digitalicagroup.example.monitor.ui.MainWindowObserver;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ControllerFXML{
	@FXML
	private Label label_result;
	@FXML
	private JFXTextField input_threads, input_max_num;
	@FXML
	private JFXSlider slider_speed;
	@FXML
	private JFXMasonryPane threads_pane;
	@FXML
	private JFXButton btn_start, btn_stop;
	@FXML 
	private AnchorPane ap;
	@FXML
	private StackPane dialog_pane;
	
	
	public ControllerFXML() {
		
	}
	
	public void initialize() {
		// Ammount of consumer (threads) to be launched and maxInteger count to be consumed.
		int consumerCount = 9, max_integer = 10;
		
		// Init inputs
		input_threads.setText(String.valueOf(consumerCount));
		input_max_num.setText(String.valueOf(max_integer));
		btn_stop.setDisable(true);
		
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("Introdução"));
		content.setBody(new Text("\n Após configurar os parâmetros disponíveis do lado esquerdo," +
								 "\n clique em START para começar a simulação.\n" + 
								 "\n Cada bloco representa uma thread, o qual mostra o número" +
								 "\n que está sendo processado pela única thread ativa, visto que" +
								 "\n cada thread compete pelo 'lock' do monitor. \n"));
		
		JFXDialog dialog = new JFXDialog(dialog_pane, content, JFXDialog.DialogTransition.CENTER);
		JFXButton btn_dialog = new JFXButton("OK");
		btn_dialog.setCursor(Cursor.HAND);
		btn_dialog.setOnAction(new EventHandler<ActionEvent>(){
		    @Override
		    public void handle(ActionEvent event){
		        dialog.close();
		    }
		});
		content.setActions(btn_dialog);
		dialog.show();
		
		// Start Button
		btn_start.setOnAction((event) -> {
			initWindow();
			btn_start.setDisable(true);
			btn_stop.setDisable(false);
		});
		
	}
	
	public void initWindow() {
		
		int consumerCount = Integer.parseInt(input_threads.getText());
		
		int waitMillis = (int) (2300 - slider_speed.getValue() * 20);
		
		int max_integer = Integer.parseInt(input_max_num.getText());

		// Instance our integer storage with some integers.
		IntegerStorage intStorage = IntegerStorage.instance(max_integer, waitMillis);

		// Launch the notifier.
		StorageNotifier emptyStorageNotifier = new StorageNotifier(intStorage); 
		(new Thread(emptyStorageNotifier)).start();

		// create main interface
		MainWindowObserver window = new MainWindowObserver(consumerCount);
		window.addIntegerStorage(intStorage);
		window.addEmptyStorageNotifier(emptyStorageNotifier);
		
		// Launch all consumer threads
		for (int i = 0; i < consumerCount; i++) {
			Random r = new Random();
			Random s1 = new Random();
			Random s2 = new Random();
			IntegerConsumer consumer = new IntegerConsumer(intStorage, i);
			
			consumer.addObserver(window);
			(new Thread(consumer)).start();
			
			Label lbl = new Label();
			
			lbl.setPrefWidth(s1.nextDouble() * 100);
			lbl.setPrefHeight(s2.nextDouble() * 100);
			lbl.setTextFill(Color.WHITE);
			lbl.setFont(Font.font(null, FontWeight.BOLD, 20));
			lbl.setAlignment(Pos.CENTER);
			lbl.setId("Label-" + Integer.toString(i));
			lbl.setStyle("-fx-background-color: rgb(" + r.nextInt(220) + "," + r.nextInt(220) + "," + r.nextInt(220) + ");");
			
			threads_pane.getChildren().add(lbl);
		}
		window.startSimulation();
		handleEvents(window, intStorage);
	}
	
	public void handleEvents(MainWindowObserver window, IntegerStorage intStorage) {
		// Stop Button
		btn_stop.setOnAction((event) -> {
			threads_pane.getChildren().clear();
			btn_start.setDisable(false);
			btn_stop.setDisable(true);
		});
		
		// Speed
		slider_speed.valueProperty().addListener((observable, oldValue, newValue) -> {
			intStorage.setWaitMillis((int) (2300 - slider_speed.getValue() * 20));
		});
	}
	
	
}
