package com.olrox.quiz.process;

import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.WrongAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SoloGameProcessTest {

    private SoloGameProcess soloGameProcess;

    @Mock
    private SoloGame soloGame;

    @BeforeEach
    public void prepare() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLifecycle() {
        QuizQuestion q1 = new QuizQuestion();
        q1.setCorrectAnswer("a1");
        q1.getWrongAnswers().add(new WrongAnswer("w1"));
        QuizQuestion q2 = new QuizQuestion();
        q2.setCorrectAnswer("a2");
        q2.getWrongAnswers().add(new WrongAnswer("w2"));
        QuizQuestion q3 = new QuizQuestion();
        q3.setCorrectAnswer("a3");
        q3.getWrongAnswers().add(new WrongAnswer("w3"));

        when(soloGame.getQuestionList()).thenReturn(List.of(q1, q2, q3));
        soloGameProcess = new SoloGameProcess(soloGame);

        assertEquals(q1, soloGameProcess.getCurrentQuestion());
        assertEquals(0, soloGameProcess.getCurrentQuestionInd());

        AnswerResult expectedResult1 = new AnswerResult();
        expectedResult1.setStatus(AnswerResult.Status.WRONG);
        expectedResult1.setQuizQuestion(q1);
        expectedResult1.setAnswer("w1");

        AnswerResult result1 = soloGameProcess.doAnswer("w1");

        assertEquals(expectedResult1, result1);
        assertEquals(q2, soloGameProcess.getCurrentQuestion());
        assertEquals(1, soloGameProcess.getCurrentQuestionInd());

        AnswerResult expectedResult2 = new AnswerResult();
        expectedResult2.setStatus(AnswerResult.Status.CORRECT);
        expectedResult2.setQuizQuestion(q2);
        expectedResult2.setAnswer("a2");

        AnswerResult result2 = soloGameProcess.doAnswer("a2");

        assertEquals(expectedResult2, result2);
        assertEquals(q3, soloGameProcess.getCurrentQuestion());
        assertEquals(2, soloGameProcess.getCurrentQuestionInd());

        AnswerResult expectedResult3 = new AnswerResult();
        expectedResult3.setStatus(AnswerResult.Status.UNKNOWN);
        expectedResult3.setQuizQuestion(q3);
        expectedResult3.setAnswer("unknown");

        AnswerResult result3 = soloGameProcess.doAnswer("unknown");

        assertEquals(expectedResult3, result3);
        assertNull(soloGameProcess.getCurrentQuestion());
        assertNull(soloGameProcess.doAnswer(""));
        assertEquals(-1, soloGameProcess.getCurrentQuestionInd());
        assertTrue(soloGameProcess.isFinished());
    }
}