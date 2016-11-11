package com.quizme.generator.models;

import javafx.scene.image.Image;

public class Question {

    private String question, answer;
    private String[] multipleChoices;
    private Image image;
    private Difficulty difficulty;
    private Topic topic;

    public Question() { }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setMultipleChoices(String[] falseAnswers) {
        this.multipleChoices = falseAnswers;
    }

    public String[] getMultipleChoices() {
        return multipleChoices;
    }

    public boolean isMultiChoice() {
        return multipleChoices != null && multipleChoices.length > 0;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return question;
    }
}