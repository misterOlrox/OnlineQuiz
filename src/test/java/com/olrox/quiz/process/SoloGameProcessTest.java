package com.olrox.quiz.process;

import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.SoloGamePrototype;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SoloGameProcessTest {

    public static final int TIMEOUT_LIMIT = 5;
    private SoloGameProcess soloGameProcess;

    @Mock
    private SoloGame soloGame;
    @Mock
    private SoloGamePrototype soloGamePrototype;

    @BeforeEach
    public void prepare() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLifecycle() {
        User participant = new User();
        participant.setUsername("Tester");

        QuizQuestion q1 = new QuizQuestion();
        q1.setCorrectAnswer("a1");
        q1.getWrongAnswers().add(new WrongAnswer("w1"));
        QuizQuestion q2 = new QuizQuestion();
        q2.setCorrectAnswer("a2");
        q2.getWrongAnswers().add(new WrongAnswer("w2"));
        QuizQuestion q3 = new QuizQuestion();
        q3.setCorrectAnswer("a3");
        q3.getWrongAnswers().add(new WrongAnswer("w3"));

        when(soloGame.getPrototype()).thenReturn(soloGamePrototype);
        when(soloGamePrototype.getQuestionList()).thenReturn(List.of(q1, q2, q3));
        when(soloGamePrototype.getTimeForQuestionInSeconds()).thenReturn(TIMEOUT_LIMIT);
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
        assertEquals(expectedResult1, soloGameProcess.getLastAnswerResult());

        AnswerResult expectedResult2 = new AnswerResult();
        expectedResult2.setStatus(AnswerResult.Status.CORRECT);
        expectedResult2.setQuizQuestion(q2);
        expectedResult2.setAnswer("a2");

        AnswerResult result2 = soloGameProcess.doAnswer("a2");

        assertEquals(expectedResult2, result2);
        assertEquals(q3, soloGameProcess.getCurrentQuestion());
        assertEquals(2, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult2, soloGameProcess.getLastAnswerResult());

        AnswerResult expectedResult3 = new AnswerResult();
        expectedResult3.setStatus(AnswerResult.Status.UNKNOWN);
        expectedResult3.setQuizQuestion(q3);
        expectedResult3.setAnswer("unknown");

        AnswerResult result3 = soloGameProcess.doAnswer("unknown");

        assertEquals(expectedResult3, result3);
        assertNull(soloGameProcess.getCurrentQuestion());
        assertNull(soloGameProcess.doAnswer(""));
        assertEquals(3, soloGameProcess.getCurrentQuestionInd());
        assertTrue(soloGameProcess.isFinished());
        assertEquals(expectedResult3, soloGameProcess.getLastAnswerResult());

        assertEquals(soloGameProcess.getResults().get(0), expectedResult1);
        assertEquals(soloGameProcess.getResults().get(1), expectedResult2);
        assertEquals(soloGameProcess.getResults().get(2), expectedResult3);
    }

    @Test
    public void testTimeout() {
        final long START_TIME = 5000L;
        final long TIMEPOINT_1= 7500L;
        final long TIMEPOINT_2= 12501L;
        final long TIMEPOINT_3= 17500L;
        Clock clock = mock(Clock.class);
        when(clock.millis()).thenReturn(START_TIME);

        User participant = new User();
        participant.setUsername("Tester");

        QuizQuestion q1 = new QuizQuestion();
        q1.setCorrectAnswer("a1");
        q1.getWrongAnswers().add(new WrongAnswer("w1"));
        QuizQuestion q2 = new QuizQuestion();
        q2.setCorrectAnswer("a2");
        q2.getWrongAnswers().add(new WrongAnswer("w2"));
        QuizQuestion q3 = new QuizQuestion();
        q3.setCorrectAnswer("a3");
        q3.getWrongAnswers().add(new WrongAnswer("w3"));

        when(soloGame.getPrototype()).thenReturn(soloGamePrototype);
        when(soloGamePrototype.getQuestionList()).thenReturn(List.of(q1, q2, q3));
        when(soloGamePrototype.getTimeForQuestionInSeconds()).thenReturn(TIMEOUT_LIMIT);
        soloGameProcess = new SoloGameProcess(soloGame, clock);

        assertEquals(q1, soloGameProcess.getCurrentQuestion());
        assertEquals(0, soloGameProcess.getCurrentQuestionInd());

        when(clock.millis()).thenReturn(TIMEPOINT_1);
        AnswerResult expectedResult1 = new AnswerResult();
        expectedResult1.setStatus(AnswerResult.Status.WRONG);
        expectedResult1.setQuizQuestion(q1);
        expectedResult1.setAnswer("w1");

        AnswerResult result1 = soloGameProcess.doAnswer("w1");

        assertEquals(expectedResult1, result1);
        assertEquals(q2, soloGameProcess.getCurrentQuestion());
        assertEquals(1, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult1, soloGameProcess.getLastAnswerResult());

        when(clock.millis()).thenReturn(TIMEPOINT_2);
        AnswerResult expectedResult2 = new AnswerResult();
        expectedResult2.setStatus(AnswerResult.Status.TIMEOUT);
        expectedResult2.setQuizQuestion(q2);
        expectedResult2.setAnswer(null);

        AnswerResult result2 = soloGameProcess.doAnswer("a2");

        assertEquals(expectedResult2, result2);
        assertEquals(q3, soloGameProcess.getCurrentQuestion());
        assertEquals(2, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult2, soloGameProcess.getLastAnswerResult());

        when(clock.millis()).thenReturn(TIMEPOINT_3);
        AnswerResult expectedResult3 = new AnswerResult();
        expectedResult3.setStatus(AnswerResult.Status.UNKNOWN);
        expectedResult3.setQuizQuestion(q3);
        expectedResult3.setAnswer("unknown");

        AnswerResult result3 = soloGameProcess.doAnswer("unknown");

        assertEquals(expectedResult3, result3);
        assertNull(soloGameProcess.getCurrentQuestion());
        assertNull(soloGameProcess.doAnswer(""));
        assertEquals(3, soloGameProcess.getCurrentQuestionInd());
        assertTrue(soloGameProcess.isFinished());
        assertEquals(expectedResult3, soloGameProcess.getLastAnswerResult());

        assertEquals(soloGameProcess.getResults().get(0), expectedResult1);
        assertEquals(soloGameProcess.getResults().get(1), expectedResult2);
        assertEquals(soloGameProcess.getResults().get(2), expectedResult3);
    }
}