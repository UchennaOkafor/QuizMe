package com.quizme.app.controllers;

import java.util.*;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.effect.GaussianBlur;
import javafx.animation.RotateTransition;

import com.quizme.app.misc.AlertBox;
import com.quizme.generator.models.Question;

public class QuizController implements Initializable {

    @FXML
    private Pane mainPane;
    @FXML
    private ImageView imageQuestion, imageViewFeedback;
    @FXML
    private Label lblQuestion, lblQuestionProgress;
    @FXML
    private TextArea txtAnswer;
    @FXML
    private ToggleGroup groupMultiChoices;

    private int score, maxNumQuestions;
    private Queue<Question> unansweredQuestions;
    private Question currentQuestion;
    private Image defaultImg;
    private RotateTransition rotateTransition;
    private MediaPlayer mediaPlayer;

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
        Platform.runLater(() -> {
            initializeMedia();
            initializeQuestions();
            initializeAnimation();
        });
    }

    /**
     * Initializes all the media objects used in the program
     */
    private void initializeMedia() {
        String resourceDirectory = "/com/quizme/app/resources";
        defaultImg = new Image(resourceDirectory + "/images/default_question_img.jpg");
        imageViewFeedback.setImage(new Image(resourceDirectory + "/images/1460367827_tick_green.png"));
        mediaPlayer = new MediaPlayer(new Media(getClass().getResource(resourceDirectory + "/sounds/answer_correct.mp3").toString()));
    }

    /**
     * Initializes the animation objects used for the spinning tick icon
     */
    private void initializeAnimation() {
        rotateTransition = new RotateTransition(Duration.millis(1000), imageViewFeedback);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);

        rotateTransition.setOnFinished(e -> {
            imageViewFeedback.setVisible(false);
            pollNextQuestion();
        });
    }

    /**
     * Initializes the generated questions
     */
    private void initializeQuestions() {
        //Gets the questions that was attached to the stage user data object property
        Stage currentStage = (Stage) imageQuestion.getScene().getWindow();
        Question[] questions = (Question[]) currentStage.getUserData();

        //Adds all questions to a queue data structure
        unansweredQuestions = new ArrayDeque<>();
        Collections.addAll(unansweredQuestions, questions);
        maxNumQuestions = unansweredQuestions.size();

        displayQuestion(unansweredQuestions.poll());
        lblQuestionProgress.setText(String.format("Question: 1/%s", maxNumQuestions));
    }

    /**
     * The event handler for when the next button is clicked
     * This event is registered in the ViewQuiz.FXML file
     * @param event the event object
     */
    @FXML
    private void btnNextOnAction(ActionEvent event) {
        String userAnswer = getUsersAnswer();

        if (userAnswer == null) {
            AlertBox.show("Quiz Me", "Missing input", "You must input an answer");
            return;
        }

        if (isAnswerCorrect(userAnswer, currentQuestion.getAnswer())) {
            imageViewFeedback.setVisible(true);
            mediaPlayer.stop();
            mediaPlayer.play();
            rotateTransition.play();
            score++;
        } else {
            AlertBox.show("Quiz Me", "Wrong Answer", "You've got the answer wrong.\n" +
                            "The correct answer is = " + currentQuestion.getAnswer());
            pollNextQuestion();
        }
    }

    /**
     * Used to move to the next question in the queue
     */
    private void pollNextQuestion() {
        if (unansweredQuestions.isEmpty()) {
            //To avoid a showAndWait error because the animation may not have fully finished yet
            Platform.runLater(() -> {
                double percentage = (score * 100f) / maxNumQuestions;
                String msg = String.format("You've scored %d out of %d which is %.2f%%", score, maxNumQuestions, percentage);
                AlertBox.show("Quiz Me", "Quiz Finished", msg);
                mainPane.setEffect(new GaussianBlur(4.2));
                mainPane.setDisable(true);
            });
        } else {
            clearUserInputs();
            displayQuestion(unansweredQuestions.poll());
            int currentQuestionNum = maxNumQuestions - unansweredQuestions.size();
            lblQuestionProgress.setText(String.format("Question: %s/%s", currentQuestionNum, maxNumQuestions));
        }
    }

    private void clearUserInputs() {
        txtAnswer.setText("");
        groupMultiChoices.selectToggle(null);
    }

    /**
     * Displays the current question to the user
     * @param question the question object to display
     */
    private void displayQuestion(Question question) {
        lblQuestion.setText(question.getQuestion());
        imageQuestion.setImage(question.getImage() == null ? defaultImg : question.getImage());

        setMultiChoiceDisplay(question.isMultiChoice());

        if (question.isMultiChoice()) {
            List<String> choices = new ArrayList<>(Arrays.asList(question.getMultipleChoices()));
            choices.add(question.getAnswer());
            Collections.shuffle(choices);

            groupMultiChoices.getToggles().forEach(toggle -> {
                ToggleButton tBtn = (ToggleButton) toggle;
                tBtn.setText(choices.remove(0));
            });
        }

        currentQuestion = question;
    }

    /**
     * Changes the visibility of certain controls depending
     * @param value the boolean to represent the controls visibility
     */
    private void setMultiChoiceDisplay(boolean value) {
        groupMultiChoices.getToggles().forEach(toggle -> {
            ToggleButton toggleBtn = (ToggleButton) toggle;
            toggleBtn.setVisible(value);
        });

        txtAnswer.setVisible(! value);
    }

    /**
     * Checks if the users answer is correct
     * @param usersAnswer the users answer to compare
     * @param actualAnswer the actual question answer
     * @return the boolean result of the operation
     */
    private boolean isAnswerCorrect(String usersAnswer, String actualAnswer) {
        String strippedUserAnswer = usersAnswer.replace(" ", "").toLowerCase();
        String strippedAnswer = actualAnswer.replace(" ", "").toLowerCase();
        return strippedUserAnswer.equals(strippedAnswer);
    }

    /**
     *
     * @return the users answer
     */
    private String getUsersAnswer() {
        if (currentQuestion.isMultiChoice()) {
            ToggleButton selectedToggle = ((ToggleButton) groupMultiChoices.getSelectedToggle());
            return selectedToggle == null ? null : selectedToggle.getText();
        } else {
            String userAnswer = txtAnswer.getText();
            return userAnswer.isEmpty() ? null : userAnswer;
        }
    }

    /**
     * The event handler for when the user presses a key whilst the txtAnswer control has focus.
     * This event is registered in the ViewQuiz.FXML file
     * @param event the mouse event
     */
    @FXML
    private void txtAnswerOnKeyReleased(KeyEvent event) {
        if (txtAnswer.getText().contains("^2")) {
            txtAnswer.setText(txtAnswer.getText().replace("^2", "²"));
            txtAnswer.positionCaret(txtAnswer.getText().length());
        }
    }
}