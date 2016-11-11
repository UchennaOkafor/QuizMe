package com.quizme.generator.modules.shapes.base;

import com.quizme.generator.base.AbstractQuestionGenerator;
import com.quizme.generator.models.Question;
import com.quizme.generator.modules.shapes.modules.TriangleQuestionGenerator;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Difficulty;
import com.quizme.generator.modules.shapes.modules.RectangleQuestionGenerator;

public class ShapesQuestionGenerator extends AbstractQuestionGenerator {

    private final String[] VERBS;
    private ShapesQuestionGenerator[] modules;

    public ShapesQuestionGenerator() {
        super(Topic.SHAPES);

        VERBS = new String[] {
            "Calculate",
            "Work out",
            "Evaluate",
            "Find"
        };
    }

    /**
     * Initializes the ShapesQuestionGenerator subclasses
     */
    private void initializeModules() {
        //Ideally the modules should be initialized in the constructors,
        //but doing so causes a stack-overflow exception due to an infinite recursive constructor call.
        //Initializing the modules like this is a workaround.
        if (modules == null) {
            modules = new ShapesQuestionGenerator[] {
                new RectangleQuestionGenerator(),
                new TriangleQuestionGenerator()
            };
        }
    }

    /**
     * Generates an array of random shapes questions
     * @param amount the amount of questions to generate
     * @param difficultyLevel the difficulty level of the questions to generate
     * @param isMultiChoice if the questions to generate are multiple choices questions
     * @return an array of the generated questions
     */
    @Override
    public Question[] generateQuestions(int amount, Difficulty difficultyLevel, boolean isMultiChoice) {
        initializeModules();

        Question[] questions = new Question[amount];

        for (int i = 0; i < amount; i++) {
            int randIndex = nextInt(0, modules.length - 1);
            ShapesQuestionGenerator randModule = modules[randIndex];
            questions[i] = randModule.generateQuestions(1, difficultyLevel, isMultiChoice)[0];
        }

        return questions;
    }

    /**
     *
     * @return a random verb
     */
    protected String getRandomVerb() {
        return VERBS[nextInt(0, VERBS.length - 1)];
    }
}