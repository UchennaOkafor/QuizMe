package com.quizme.generator.base;

import java.util.Random;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;

/**
 * The base class for all question generator modules.
 * All question generator modules must extend this abstract class.
 */
public abstract class AbstractQuestionGenerator {

    private final Topic TOPIC;
    private Random rand;

    /**
     * Creates a new instance of the abstract question generator class
     * @param topic the topic type to associate the question generator class with
     */
    public AbstractQuestionGenerator(Topic topic) {
        this.TOPIC = topic;
        this.rand = new Random();
    }

    /**
     * Generates an array of random questions
     * @param amount the amount of questions to generate
     * @param difficultyLevel the difficulty level of the questions to generate
     * @param isMultiChoice if the questions to generate are multiple choices questions
     * @return an array of the generated questions
     */
    public abstract Question[] generateQuestions(int amount, Difficulty difficultyLevel, boolean isMultiChoice);

    /**
     *
     * @return the topic value of the current class
     */
    public final Topic getTopic() {
        return TOPIC;
    }

    /**
     * Returns a pseudo random integer between a specified range
     * @param min the lower bound
     * @param max the upper bound
     * @return the pseudo random generated number
     */
    protected final int nextInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Returns the next pseudo random boolean value
     * @return pseudo random boolean value
     */
    protected final boolean nextBoolean() {
        return rand.nextBoolean();
    }
}