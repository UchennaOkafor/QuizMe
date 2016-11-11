package com.quizme.app.controllers;

import java.net.URL;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.quizme.app.misc.AlertBox;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;
import com.quizme.generator.provider.QuestionGenerator;

public class SetupController implements Initializable {

    @FXML
    private ComboBox<String> cboTopics;
    @FXML
    private TextField txtQuestionAmount;
    @FXML
    private CheckBox chkMultiChoice;
    @FXML
    private ToggleGroup difficultyGroup;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cboTopics.getItems().addAll("Mixed", "Shapes", "Algebra");
    }

    /**
     * An event handler for when the start button is clicked
     * This event is registered in the ViewSetup.FXML file
     * @param event the event object
     */
    @FXML
    private void btnStartOnAction(ActionEvent event) {
        String errorMsg = getValidationMessage();

        if (errorMsg != null) {
            AlertBox.show("Quiz Me", "Incorrect user input", errorMsg);
            return;
        }

        Topic topic = getSelectedTopic();
        Difficulty difficulty = getSelectedDifficulty();
        boolean isMultiChoice = chkMultiChoice.isSelected();
        int amount = Integer.parseInt(txtQuestionAmount.getText());

        QuestionGenerator generator = new QuestionGenerator();
        Question[] questions;

        if (topic == null) {
            questions = generator.generateQuestions(difficulty, amount, isMultiChoice);
        } else {
            questions = generator.generateQuestions(topic, difficulty, amount, isMultiChoice);
        }

        loadQuizScene(questions);
    }

    /**
     *
     * @return the users selected difficulty level
     */
    private Difficulty getSelectedDifficulty() {
        ToggleButton selectedTButton = (ToggleButton) difficultyGroup.getSelectedToggle();
        return Difficulty.valueOf(selectedTButton.getText().toUpperCase());
    }

    /**
     *
     * @return the users selected topic
     */
    private Topic getSelectedTopic() {
        try {
            return Topic.valueOf(cboTopics.getSelectionModel().getSelectedItem().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Evaluates a number to be valid by using a Regular Expression
     * @param input the number to validate
     * @return the boolean result of the operation
     */
    private boolean isValidNumber(String input) {
        return Pattern.matches("^[0-9]{1,2}$", input);
    }

    /**
     *
     * @return the validation error message is any is present, if it's not it returns null
     */
    private String getValidationMessage() {
        String errorMsg = null;

        if (cboTopics.getSelectionModel().getSelectedIndex() == -1) {
            errorMsg = "You must select a topic";
        }

        if (difficultyGroup.getSelectedToggle() == null) {
            errorMsg = "You must select a difficulty level";
        }

        if (! isValidNumber(txtQuestionAmount.getText())) {
            errorMsg = "Amount must be a valid integer between 1 and 99";
        }

        return errorMsg;
    }

    /**
     * Loads the next stage/window
     * @param generatedQuestions the list of generated questions to associate with the next stage/window
     */
    private void loadQuizScene(Question[] generatedQuestions) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/quizme/app/views/ViewQuiz.fxml"));
            Stage stage = new Stage();
            stage.setTitle("QuizMe - Quiz Setup");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("com/quizme/app/resources/images/main-icon.png"));
            stage.setResizable(false);
            stage.sizeToScene();

            //Passes the list of generated questions to the next Stage(JavaFX equivalent to Jframe)
            //This can then be retrieved from the next Stage's controller class
            stage.setUserData(generatedQuestions);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}