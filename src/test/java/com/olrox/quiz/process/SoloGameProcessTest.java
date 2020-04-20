package com.olrox.quiz.process;

import com.olrox.quiz.entity.UserAnswer;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.GamePrototype;
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
    private GamePrototype soloGamePrototype;

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
        when(soloGamePrototype.getShuffledQuestionList()).thenReturn(List.of(q1, q2, q3));
        when(soloGamePrototype.getTimeForQuestionInSeconds()).thenReturn(TIMEOUT_LIMIT);
        soloGameProcess = new SoloGameProcess(soloGame);

        assertEquals(q1, soloGameProcess.getCurrentQuestion());
        assertEquals(0, soloGameProcess.getCurrentQuestionInd());

        UserAnswer expectedResult1 = new UserAnswer();
        expectedResult1.setStatus(UserAnswer.Status.WRONG);
        expectedResult1.setQuizQuestion(q1);
        expectedResult1.setAnswer("w1");
        expectedResult1.setGame(soloGame);

        UserAnswer result1 = soloGameProcess.doAnswer("w1");

        assertEquals(expectedResult1, result1);
        assertEquals(q2, soloGameProcess.getCurrentQuestion());
        assertEquals(1, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult1, soloGameProcess.getLastAnswerResult());

        UserAnswer expectedResult2 = new UserAnswer();
        expectedResult2.setStatus(UserAnswer.Status.CORRECT);
        expectedResult2.setQuizQuestion(q2);
        expectedResult2.setAnswer("a2");
        expectedResult2.setGame(soloGame);

        UserAnswer result2 = soloGameProcess.doAnswer("a2");

        assertEquals(expectedResult2, result2);
        assertEquals(q3, soloGameProcess.getCurrentQuestion());
        assertEquals(2, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult2, soloGameProcess.getLastAnswerResult());

        UserAnswer expectedResult3 = new UserAnswer();
        expectedResult3.setStatus(UserAnswer.Status.UNKNOWN);
        expectedResult3.setQuizQuestion(q3);
        expectedResult3.setAnswer("unknown");
        expectedResult3.setGame(soloGame);

        UserAnswer result3 = soloGameProcess.doAnswer("unknown");

        assertEquals(expectedResult3, result3);
        assertNull(soloGameProcess.getCurrentQuestion());
        assertNull(soloGameProcess.doAnswer(""));
        assertEquals(3, soloGameProcess.getCurrentQuestionInd());
        assertTrue(soloGameProcess.isFinished());
        assertEquals(expectedResult3, soloGameProcess.getLastAnswerResult());

        assertEquals(soloGameProcess.getUserAnswers().get(0), expectedResult1);
        assertEquals(soloGameProcess.getUserAnswers().get(1), expectedResult2);
        assertEquals(soloGameProcess.getUserAnswers().get(2), expectedResult3);
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
        when(soloGamePrototype.getShuffledQuestionList()).thenReturn(List.of(q1, q2, q3));
        when(soloGamePrototype.getTimeForQuestionInSeconds()).thenReturn(TIMEOUT_LIMIT);
        soloGameProcess = new SoloGameProcess(soloGame, clock);

        assertEquals(q1, soloGameProcess.getCurrentQuestion());
        assertEquals(0, soloGameProcess.getCurrentQuestionInd());

        when(clock.millis()).thenReturn(TIMEPOINT_1);
        UserAnswer expectedResult1 = new UserAnswer();
        expectedResult1.setStatus(UserAnswer.Status.WRONG);
        expectedResult1.setQuizQuestion(q1);
        expectedResult1.setAnswer("w1");
        expectedResult1.setGame(soloGame);

        UserAnswer result1 = soloGameProcess.doAnswer("w1");

        assertEquals(expectedResult1, result1);
        assertEquals(q2, soloGameProcess.getCurrentQuestion());
        assertEquals(1, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult1, soloGameProcess.getLastAnswerResult());

        when(clock.millis()).thenReturn(TIMEPOINT_2);
        UserAnswer expectedResult2 = new UserAnswer();
        expectedResult2.setStatus(UserAnswer.Status.TIMEOUT);
        expectedResult2.setQuizQuestion(q2);
        expectedResult2.setAnswer(null);
        expectedResult2.setGame(soloGame);

        UserAnswer result2 = soloGameProcess.doAnswer("a2");

        assertEquals(expectedResult2, result2);
        assertEquals(q3, soloGameProcess.getCurrentQuestion());
        assertEquals(2, soloGameProcess.getCurrentQuestionInd());
        assertEquals(expectedResult2, soloGameProcess.getLastAnswerResult());

        when(clock.millis()).thenReturn(TIMEPOINT_3);
        UserAnswer expectedResult3 = new UserAnswer();
        expectedResult3.setStatus(UserAnswer.Status.UNKNOWN);
        expectedResult3.setQuizQuestion(q3);
        expectedResult3.setAnswer("unknown");
        expectedResult3.setGame(soloGame);

        UserAnswer result3 = soloGameProcess.doAnswer("unknown");

        assertEquals(expectedResult3, result3);
        assertNull(soloGameProcess.getCurrentQuestion());
        assertNull(soloGameProcess.doAnswer(""));
        assertEquals(3, soloGameProcess.getCurrentQuestionInd());
        assertTrue(soloGameProcess.isFinished());
        assertEquals(expectedResult3, soloGameProcess.getLastAnswerResult());

        assertEquals(soloGameProcess.getUserAnswers().get(0), expectedResult1);
        assertEquals(soloGameProcess.getUserAnswers().get(1), expectedResult2);
        assertEquals(soloGameProcess.getUserAnswers().get(2), expectedResult3);
    }
}