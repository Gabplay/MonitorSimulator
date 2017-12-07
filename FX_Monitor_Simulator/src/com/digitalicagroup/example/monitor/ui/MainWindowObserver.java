package com.digitalicagroup.example.monitor.ui;
import java.util.Observable;
import java.util.Random;

import com.digitalicagroup.example.monitor.IntegerConsumer;
import com.digitalicagroup.example.monitor.IntegerStorage;
import com.digitalicagroup.example.monitor.SingletonObjects;
import com.digitalicagroup.example.monitor.StorageNotifier;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXMasonryPane;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainWindowObserver implements OutputManager {

	public IntegerStorage storage;
	public StorageNotifier emptyStorageNotifier;
	public int threadsQuantity;
	public Random random;

	public MainWindowObserver(int threadsQuantity) {
		this.threadsQuantity = threadsQuantity;
		this.random = new Random();
	}

	protected Color getRandomColor() {
		return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == emptyStorageNotifier) {
			AnchorPane root = SingletonObjects.INSTANCE.getRoot();
			StackPane dialog_pane = (StackPane) root.lookup("#dialog_pane");
			
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Fim"));
			content.setBody(new Text("\n Todos os números inteiros foram processados! \n"));
			
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
		} else {
			updatePanel(o, arg);
		}
	}

	protected synchronized void updatePanel(Observable consumer, Object consumedInt) {
		Platform.runLater(() -> {
			int id = ((IntegerConsumer) consumer).getId();

			AnchorPane root = SingletonObjects.INSTANCE.getRoot();

			JFXMasonryPane pane = (JFXMasonryPane) root.lookup("#threads_pane");

			Label lbl = (Label) pane.lookup("#Label-" + Integer.toString(id));
			lbl.setText(Integer.toString((int) consumedInt));
		});
	}

	@Override
	public void addIntegerStorage(IntegerStorage storage) {
		this.storage = storage;
	}

	@Override
	public void startSimulation() {
		storage.setStarted(true);
	}

	public void addEmptyStorageNotifier(StorageNotifier emptyStorageNotifier) {
		this.emptyStorageNotifier = emptyStorageNotifier;
		this.emptyStorageNotifier.addObserver(this);
	}
}
