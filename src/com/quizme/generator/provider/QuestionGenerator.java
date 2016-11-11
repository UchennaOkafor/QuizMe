package com.quizme.generator.provider;

import java.util.*;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;
import com.quizme.generator.base.AbstractQuestionGenerator;
import com.quizme.generator.modules.algebra.AlgebraQuestionGenerator;
import com.quizme.generator.modules.shapes.base.ShapesQuestionGenerator;

/**
 * A class that generate questions using question generator modules/subclasses
 */
public class QuestionGenerator {

    private List<AbstractQuestionGenerator> generatorModules;
    private Random rand;

    public QuestionGenerator() {
        rand = new Random();

        AbstractQuestionGenerator[] modules = {
            new AlgebraQuestionGenerator(),
            new ShapesQuestionGenerator()
        };

        generatorModules = Arrays.asList(modules);
    }

    /**
     * Returns the AbstractQuestionGenerator module that is of the topic requested
     * @param topic the topic to look for
     * @return The first instance of the AbstractQuestionGenerator module
     */
    private AbstractQuestionGenerator findModuleByTopic(Topic topic) {
        return generatorModules.stream().filter(module -> module.getTopic() == topic).findFirst().orElse(null);
    }

    /**
     * Generates a list of questions based on the parameters values
     * @param topic The topic the generated questions should be of
     * @param difficultyLevel The difficulty level the generated questions should be
     * @param amount The amount of questions to generate
     * @param isMultiChoice A flag to denote if the generated questions should be multiple choice questions
     * @return A list of generated questions
     */
    public Question[] generateQuestions(Topic topic, Difficulty difficultyLevel, int amount, boolean isMultiChoice) {
        return findModuleByTopic(topic).generateQuestions(amount, difficultyLevel, isMultiChoice);
    }

    /**
     * Generates a list of random topic questions based on the parameters values
     * @param difficultyLevel The difficulty level the generated questions should be
     * @param amount The amount of questions to generate
     * @param isMultiChoice A flag to denote if the generated questions should be multiple choice questions
     * @return A list of generated questions
     */
    public Question[] generateQuestions(Difficulty difficultyLevel, int amount, boolean isMultiChoice) {
        Question[] questions = new Question[amount];

        for (int i = 0; i < amount; i++) {
            //Uses a random question generator sub class when a topic isn't specified
            AbstractQuestionGenerator module = generatorModules.get(rand.nextInt(generatorModules.size()));
            questions[i] = module.generateQuestions(1, difficultyLevel, isMultiChoice)[0];
        }

        return questions;
    }
}