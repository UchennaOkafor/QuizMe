package com.quizme.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/quizme/app/views/ViewSetup.fxml"));
        primaryStage.setTitle("QuizMe - Setup");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("com/quizme/app/resources/images/main-icon.png"));

        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
