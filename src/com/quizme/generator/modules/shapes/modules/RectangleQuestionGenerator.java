package com.quizme.generator.modules.shapes.modules;

import com.quizme.generator.models.Difficulty;
import com.quizme.generator.models.Question;
import com.quizme.generator.modules.shapes.base.ShapesQuestionGenerator;
import com.quizme.generator.modules.shapes.models.Metrics;
import com.quizme.generator.modules.shapes.models.Units;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class RectangleQuestionGenerator extends ShapesQuestionGenerator {

    /**
     * Generates an array of random rectangle shapes questions
     * @param amount the amount of questions to generate
     * @param difficultyLevel the difficulty level of the questions to generate
     * @param isMultiChoice if the questions to generate are multiple choices questions
     * @return an array of the generated questions
     */
    @Override
    public Question[] generateQuestions(int amount, Difficulty difficultyLevel, boolean isMultiChoice) {

        Question[] questions = new Question[amount];

        for (int i = 0; i < amount; i++) {
            questions[i] = generateQuestion(difficultyLevel, isMultiChoice);
        }

        return questions;
    }

    /**
     * Generates a randomized rectangle question
     * @param difficulty the difficulty level of the question
     * @param isMultiChoice a flag denote if the question is a multiple choice question
     * @return the generated question object
     */
    private Question generateQuestion(Difficulty difficulty, boolean isMultiChoice) {
        Units units = Units.getRandom();
        Metrics metrics = Metrics.getRandom();
        Side sides = getSides(difficulty);

        Question question = new Question();
        question.setQuestion(createQuestionString(metrics));
        question.setImage(drawRectangle(units, sides));
        question.setAnswer(solveQuestion(sides, metrics, units));
        question.setTopic(super.getTopic());
        question.setDifficulty(difficulty);

        if (isMultiChoice) {
            String[] multiChoices = createMultiChoiceAnswers(sides, units, metrics);
            question.setMultipleChoices(multiChoices);
        }

        return question;
    }

    /**
     * Creates the multiple choice options for a multiple choice question based on parameter input
     * @param side the two sides the actual answer is based on
     * @param units the units the actual answer is based on
     * @param metrics the metrics used for the actual answer
     * @return an array of multiple choice options
     */
    private String[] createMultiChoiceAnswers(Side side, Units units, Metrics metrics) {
        String answer1;
        char superScriptTwoUnicode = '\u00B2';

        //To throw the user off
        if (metrics == Metrics.PERIMETER) {
            answer1 = String.format("%s%s%c", side.getHeight() * side.getWidth(), units, superScriptTwoUnicode);
        } else {
            answer1 = String.format("%s%s", (side.getHeight() * 2) + (side.getWidth() * 2), units);
        }

        String answer2 = String.format("%s%s%c", side.getHeight() + side.getWidth() + nextInt(2, 10), units, superScriptTwoUnicode);
        String answer3 = String.format("%s%s", side.getHeight() + side.getWidth() - nextInt(2, 15), units);

        return new String[] {
            answer1.toLowerCase(),
            answer2.toLowerCase(),
            answer3.toLowerCase()
        };
    }

    /**
     * Draws an image of a rectangle, labels each side of the rectangle and returns the image
     * @param units the units of measurements to use
     * @param side the sides for the rectangle
     * @return the rectangle image
     */
    private Image drawRectangle(Units units, Side side) {
        Canvas canvas = new Canvas(350, 350);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.setLineWidth(1);
        gc.fillRect(50, 50, 180, 260);

        gc.setFont(Font.font(25));
        gc.setFill(Color.RED);

        //Labels the width and length of the rectangle
        String lowerUnits = units.toString().toLowerCase();
        gc.fillText(side.getWidth() + lowerUnits, 110, 40);
        gc.fillText(side.getHeight() + lowerUnits, 240, 180);

        //Rotates the image to give it a more random feel
        canvas.setRotate(nextInt(-90, 90));
        canvas.autosize();
        return canvas.snapshot(null, null);
    }

    /**
     * @param metrics the metrics of the question e.g. Area or Perimeter
     * @return the string representation of the question
     */
    private String createQuestionString(Metrics metrics) {
        return String.format("%s the %s of this rectangle", getRandomVerb(), metrics.toString().toLowerCase());
    }

    /**
     * Solves the given question based on the parameter values
     * @param side the width and length of the rectangle
     * @param metrics the metrics, e.g. Area or Perimeter
     * @param units the unit of measurement used
     * @return the string representation of the answer
     */
    private String solveQuestion(Side side, Metrics metrics, Units units) {
        int answer;

        if (metrics == Metrics.PERIMETER) {
            answer = (side.getHeight() * 2) + (side.getWidth() * 2);
        } else {
            answer = (side.getHeight() * side.getWidth());
        }

        char power = (metrics == Metrics.AREA) ? '\u00B2' : Character.MIN_VALUE;
        return String.format("%d%s%s", answer, units.toString().toLowerCase(), power);
    }

    /**
     * Generates the width and length of a rectangle based on the difficulty level param
     * @param difficultyLevel the difficulty level of the generated length and width
     * @return the two side values of a rectangle
     */
    private Side getSides(Difficulty difficultyLevel) {
        int width, length;
        int maxWidth, maxLength;

        switch (difficultyLevel) {
            case EASY:
                maxWidth = 21;
                maxLength = 43;
                break;

            case MEDIUM:
                maxWidth = 51;
                maxLength = 65;
                break;

            case HARD:
            default:
                maxWidth = 69;
                maxLength = 90;
                break;
        }

        //The while loop is to ensure the length is always greater than width
        //This is due to the basic principals of maths. The length is always greater than width
        do {
            width = nextInt(3, maxWidth);
            length = nextInt(3, maxLength);
        } while (length < width);

        return new Side(width, length);
    }

    /**
     * Represents the width and height of a rectangle
     */
    private class Side {

        private int width;
        private int height;

        public Side(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}