package com.quizme.generator.modules.shapes.modules;

import com.quizme.generator.modules.shapes.models.Units;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;
import com.quizme.generator.modules.shapes.models.Metrics;
import com.quizme.generator.modules.shapes.base.ShapesQuestionGenerator;

public class TriangleQuestionGenerator extends ShapesQuestionGenerator {

    /**
     * Generates an array of random triangle shapes questions
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
     * Generates a randomized triangle question
     * @param difficulty the difficulty level of the question
     * @param isMultiChoice a flag denote if the question is a multiple choice question
     * @return the generated question object
     */
    private Question generateQuestion(Difficulty difficulty, boolean isMultiChoice) {
        Units units = Units.getRandom();
        Metrics metrics = Metrics.getRandom();
        TriangleSides sides = getSides(difficulty);

        Question question = new Question();
        question.setQuestion(createQuestionString(metrics));
        question.setImage(drawTriangle(units, sides));
        question.setAnswer(solveQuestion(sides, metrics, units));
        question.setTopic(super.getTopic());
        question.setDifficulty(difficulty);

        if (isMultiChoice) {
            question.setMultipleChoices(createMultiChoiceAnswers(sides, units, metrics));
        }

        return question;
    }

    /**
     * Creates the multiple choice options for a multiple choice question based on parameter input
     * @param side the three sides the actual answer is based on
     * @param units the units the actual answer is based on
     * @param metrics the metrics used for the actual answer
     * @return an array of multiple choice options
     */
    private String[] createMultiChoiceAnswers(TriangleSides side, Units units, Metrics metrics) {
        String answer1, answer2;
        char superScriptTwoUnicode = '\u00B2';

        //To throw the user off
        if (metrics == Metrics.PERIMETER) {
            answer1 = String.format("%.2f%s%c", (double) (side.getAdjacent() * side.getOpposite()) / 2, units, superScriptTwoUnicode);
            answer2 = String.format("%.2f%s%c", (double) side.getOpposite() + side.getAdjacent() + side.getHypotenuse(), units, superScriptTwoUnicode);
        } else {
            answer1 = String.format("%.2f%s", (double) side.getHypotenuse() + side.getAdjacent() + side.getOpposite(), units);
            answer2 = String.format("%.2f%s%c", (double) side.getOpposite() + side.getAdjacent(), units, superScriptTwoUnicode);
        }

        String answer3 = String.format("%.2f%s", (double) side.getOpposite() + side.getOpposite() + nextInt(2, 10), units);

        return new String[] {
            answer1.toLowerCase(),
            answer2.toLowerCase(),
            answer3.toLowerCase()
        };
    }

    /**
     * Draws an image of a right angled triangle, labels each side of the triangle and returns the image
     * @param units the units of measurements to use
     * @param side the sides for the triangle
     * @return the triangle image
     */
    private Image drawTriangle(Units units, TriangleSides side) {
        Canvas canvas = new Canvas(350, 350);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.setLineWidth(1);

        gc.setFont(Font.font(25));
        gc.setFill(Color.RED);

        gc.fillPolygon(new double[]{100, 100, 260}, new double[]{70, 270, 270}, 3);

        String lowerUnits = units.toString().toLowerCase();
        gc.fillText(side.getAdjacent() + lowerUnits, 40, 180);
        gc.fillText(side.getHypotenuse() + lowerUnits, 180, 160);
        gc.fillText(side.getOpposite() + lowerUnits, 145, 300);

        canvas.setRotate(nextInt(-90, 90));
        return canvas.snapshot(null, null);
    }

    /**
     * @param metrics the metrics of the question e.g. Area or Perimeter
     * @return the string representation of the question
     */
    private String createQuestionString(Metrics metrics) {
        return String.format("%s the %s of this triangle", getRandomVerb(), metrics.toString().toLowerCase());
    }

    /**
     * Solves the given question based on the parameter values
     * @param side the three sides of the triangle
     * @param metrics the metrics, e.g. Area or Perimeter
     * @param units the unit of measurement used
     * @return the string representation of the answer
     */
    private String solveQuestion(TriangleSides side, Metrics metrics, Units units) {
        double answer;

        if (metrics == Metrics.PERIMETER) {
            answer = side.getHypotenuse() + side.getAdjacent() + side.getOpposite();
        } else {
            answer = (side.getAdjacent() * side.getOpposite()) / 2;
        }

        char power = (metrics == Metrics.AREA) ? '\u00B2' : Character.MIN_VALUE;
        return String.format("%.2f%s%s", answer, units.toString().toLowerCase(), power);
    }

    /**
     * Generates the width and length of a triangle based on the difficulty level param
     * @param difficultyLevel the difficulty level of the generated length and width
     * @return the three sides of a triangle
     */
    private TriangleSides getSides(Difficulty difficultyLevel) {
        int hypotenuse, adjacent, opposite;
        int maxHypotenuse, maxAdjacent, maxOpposite;

        switch (difficultyLevel) {
            case EASY:
                maxHypotenuse = 25;
                maxAdjacent = 15;
                maxOpposite = 5;
                break;

            case MEDIUM:
                maxHypotenuse = 35;
                maxAdjacent = 15;
                maxOpposite = 4;
                break;

            case HARD:
            default:
                maxHypotenuse = 42;
                maxAdjacent = 22;
                maxOpposite = 11;
                break;
        }

        do {
            hypotenuse = nextInt(3, maxHypotenuse);
            adjacent = nextInt(3, maxAdjacent);
            opposite = nextInt(3, maxOpposite);
        } while (hypotenuse > adjacent && adjacent > opposite);

        return new TriangleSides(hypotenuse, adjacent, opposite);
    }

    /**
     * Represents the three sides of a triangle
     */
    private class TriangleSides {

        private int hypotenuse;
        private int adjacent;
        private int opposite;

        public TriangleSides(int hypotenuse, int adjacent, int opposite) {
            this.hypotenuse = hypotenuse;
            this.adjacent = adjacent;
            this.opposite = opposite;
        }

        public int getHypotenuse() {
            return hypotenuse;
        }

        public int getAdjacent() {
            return adjacent;
        }

        public int getOpposite() {
            return opposite;
        }
    }
}
