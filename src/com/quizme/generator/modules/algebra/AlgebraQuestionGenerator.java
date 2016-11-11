package com.quizme.generator.modules.algebra;

import com.quizme.generator.base.AbstractQuestionGenerator;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;

public class AlgebraQuestionGenerator extends AbstractQuestionGenerator {

    private final String[] WORD_STARTERS;

    public AlgebraQuestionGenerator() {
        super(Topic.ALGEBRA);

        WORD_STARTERS = new String[]{
            "Simplify", "Evaluate",
            "What is", "Work out",
            "What is the answer to",
            "Multiply out these pair of brackets",
            "Expand the following brackets"
        };
    }

    /**
     * Generates an array of random algebra questions
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
     * Generates a randomized algebra question
     * @param difficultyLevel the difficulty level of the question
     * @param isMultiChoice a flag denote if the question is a multiple choice question
     * @return the generated question object
     */
    private Question generateQuestion(Difficulty difficultyLevel, boolean isMultiChoice) {
        Question q = new Question();
        Bracket[] brackets = generateBracketPair();

        String question = getRandomStarter() + " " + brackets[0].toString() + brackets[1].toString();
        q.setQuestion(question);
        q.setAnswer(solveAnswer(brackets[0], brackets[1]));
        q.setTopic(super.getTopic());
        q.setDifficulty(difficultyLevel);

        if (isMultiChoice) {
            q.setMultipleChoices(createMultipleChoices(brackets[0], brackets[1]));
        }

        return q;
    }

    /**
     * Solves the answer of an equation based on two bracket pairs
     * @param bracket1 the first bracket
     * @param bracket2 the second bracket
     * @return the answer of the equation
     */
    private String solveAnswer(Bracket bracket1, Bracket bracket2) {
        //ax^2 + bx + c 'This is how the format/formula of what the answer would be like.
        int b = bracket1.getNumber() + bracket2.getNumber();
        int c = bracket1.getNumber() * bracket2.getNumber();
        char variable = bracket1.getVariable();

        return answerToString(variable, b, c);
    }

    /**
     * Creates the multiple choice answers for multiple choice questions based of the actual question answer
     * @param bracket1 bracket1
     * @param bracket2 bracket2
     * @return an array of multiple choices
     */
    private String[] createMultipleChoices(Bracket bracket1, Bracket bracket2) {
        String[] falseAnswers = new String[3];
        char variable = bracket1.getVariable();

        //The false answers are created around the real answer in order to make it harder
        //for the user to guess the correct answer

        for (int i = 0; i < falseAnswers.length; i++) {
            int b = bracket1.getNumber() + bracket2.getNumber();
            int c = (bracket1.getNumber() * bracket2.getNumber()) + nextInt(-5, 5);
            c = nextBoolean() ? c * -1 : c;

            falseAnswers[i] = answerToString(variable, b, c);
        }

        return falseAnswers;
    }

    /**
     * Formats the mathematical variables into the answer
     * @param variable the bracket variable
     * @param b the b value
     * @param c the c value
     * @return the mathematical representation of the answer
     */
    private String answerToString(char variable, int b, int c) {
        StringBuilder sb = new StringBuilder();

        //e.g. x^2
        sb.append(variable).append('\u00B2');

        if (b != 0) {
            //e.g. e.g. x^2 + 5b
            sb.append(b > 0 ? " + " : " - ").append(Math.abs(b)).append(variable);
        }

        if (c != 0) {
            //e.g. e.g. x^2 + 5b - 6
            sb.append(c > 0 ? " + " : " - ").append(Math.abs(c));
        }

        return sb.toString();
    }

    /**
     * Gets a random sentence starter from the word starter array
     * @return random sentence starter
     */
    private String getRandomStarter(){
        return WORD_STARTERS[nextInt(0, WORD_STARTERS.length - 1)];
    }

    /**
     * Generates a random pair of the algebra bracket object
     * @return the generated bracket pair
     */
    private Bracket[] generateBracketPair() {
        char[] variables = new char[] {'a', 'b', 'x', 'y', 'n', 'z'};
        char chosenVariable = variables[nextInt(0, variables.length - 1)];
        int bracket1Val = 0, bracket2Val = 0;

        //This is to ensure that both bracket values cannot both be 0
        while (bracket1Val + bracket2Val == 0) {
            bracket1Val = nextInt(-20, 20);
            bracket2Val = nextInt(-20, 20);
        }

        return new Bracket[] {
            new Bracket(chosenVariable, bracket1Val),
            new Bracket(chosenVariable, bracket2Val)
        };
    }

    /**
     * Represents an algebra maths bracket e.g. (x+3)
     */
    private class Bracket {
        private char variable; // e.g. a|b|y|z|n|x
        private int number;

        public Bracket(char variable, int number) {
            this.variable = variable;
            this.number = number;
        }

        public char getVariable() {
            return variable;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return String.format("(%s %s %s)", variable, number > 0 ? '+' : '-', Math.abs(number));
        }
    }
}