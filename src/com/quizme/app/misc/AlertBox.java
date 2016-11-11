package com.quizme.app.misc;

import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;

/**
 * A static wrapper class used to simplifying alert boxes
 */
public class AlertBox {

    /**
     * Displays an alert box
     * @param title the title of the alert box
     * @param headerText the header text of the alert box
     * @param contentText the content text of the alert box
     * @param alertType the alertType of the alert box
     */
    public static void show(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Displays an alert box
     * @param title the title of the alert box
     * @param headerText the header text of the alert box
     * @param contentText the content text of the alert box
     */
    public static void show(String title, String headerText, String contentText) {
        show(title, headerText, contentText, Alert.AlertType.INFORMATION);
    }
}