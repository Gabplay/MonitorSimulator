package com.digitalicagroup.example.monitor;

import java.util.Observable;

import javafx.application.Platform;

public class StorageNotifier extends Observable implements Runnable {

	private IntegerStorage intStorage;

	public StorageNotifier(IntegerStorage intStorage) {
		this.intStorage = intStorage;
	}

	@Override
	public void run() {
		try {
			intStorage.waitForAllThreads();
			Platform.runLater(() -> {
				setChanged();
				notifyObservers();
			});
		} catch (InterruptedException ignored) {
		}
	}

}
