package com.quizme.test;

import org.junit.*;
import com.quizme.generator.models.Topic;
import com.quizme.generator.models.Question;
import com.quizme.generator.models.Difficulty;
import com.quizme.generator.provider.QuestionGenerator;

public class QuestionGeneratorTest {

    //This rule ensures that all com.quizme.test run on a JavaFX thread since it's a JavaFX application.
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private QuestionGenerator questionGenerator;

    @Before
    public void initializeQuestionGenerator() {
        questionGenerator = new QuestionGenerator();
    }

    @Test
    public void testQuestionGeneratorGeneratesRequestedAmountOfQuestions() throws Exception {
        int requestedAmount = 15;
        Question[] questions = questionGenerator.generateQuestions(Difficulty.MEDIUM, requestedAmount, true);
        Assert.assertTrue(questions.length == requestedAmount);
    }

    @Test
    public void testQuestionGeneratorGeneratesQuestionsWithRequestedTopic() throws Exception {
        Topic desiredTopic = Topic.SHAPES;
        Question[] questions = questionGenerator.generateQuestions(desiredTopic, Difficulty.EASY, 15, true);
        boolean topicUnchanged = true;

        for (Question q : questions) {
            if (q.getTopic() != desiredTopic) {
                topicUnchanged = false;
                break;
            }
        }

        Assert.assertTrue(topicUnchanged);
    }

    @Test
    public void testQuestionGeneratorCanGenerateAllMultipleChoiceQuestions() throws Exception {
        boolean isMultiChoice = true;
        Question[] questions = questionGenerator.generateQuestions(Topic.SHAPES, Difficulty.EASY, 15, isMultiChoice);
        boolean allQuestionsMultiChoice = true;

        for (Question q : questions) {
            if (! q.isMultiChoice()) {
                allQuestionsMultiChoice = false;
                break;
            }
        }

        Assert.assertTrue(allQuestionsMultiChoice);
    }

    @Test
    public void testQuestionGeneratorCanGenerateQuestionsWithRequestedDifficulty() throws Exception {
        Difficulty requestedDifficulty = Difficulty.MEDIUM;
        Question[] questions = questionGenerator.generateQuestions(requestedDifficulty, 25, true);
        boolean difficultyChanged = false;

        for (Question q : questions) {
            if (q.getDifficulty() != requestedDifficulty) {
                difficultyChanged = true;
            }
        }

        Assert.assertFalse(difficultyChanged);
    }

    @Test
    public void testQuestionGeneratorCanGenerateMixedTopicQuestions() throws Exception {
        Question[] questions = questionGenerator.generateQuestions(Difficulty.EASY, 25, true);
        boolean differentTopic = false;
        Topic previousTopic = null;

        for (Question q : questions) {
            if (previousTopic != null && previousTopic != q.getTopic()) {
                differentTopic = true;
                break;
            }
            previousTopic = q.getTopic();
        }

        Assert.assertTrue(differentTopic);
    }
}