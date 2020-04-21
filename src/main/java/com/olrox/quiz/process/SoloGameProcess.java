package com.olrox.quiz.process;

import com.olrox.quiz.dto.QuestionToGameDto;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.UserAnswer;
import com.olrox.quiz.entity.WrongAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SoloGameProcess {

    public static final Logger LOG = LoggerFactory.getLogger(SoloGameProcess.class);

    private final SoloGame soloGame;
    private final List<QuestionToGameDto> questionDtos;
    private final List<QuizQuestion> questionList;
    private final List<UserAnswer> userAnswers;
    private final Clock clock;
    private final long timeForQuestionInMillis;
    private final long startTimeInMillis;

    private volatile int currentQuestionInd;
    private volatile boolean finished = false;
    private volatile long lastTimestamp;

    public SoloGameProcess(SoloGame soloGame) {
        this(soloGame, Clock.systemDefaultZone());
    }

    protected SoloGameProcess(SoloGame soloGame, Clock clock) {
        this.soloGame = soloGame;
        this.questionList = soloGame.getPrototype().getShuffledQuestionList();
        this.questionDtos = questionList.stream().map(QuestionToGameDto::new).collect(Collectors.toList());
        this.currentQuestionInd = 0;
        this.userAnswers = new ArrayList<>();
        this.clock = clock;
        this.lastTimestamp = this.clock.millis();
        this.timeForQuestionInMillis = soloGame.getPrototype().getTimeForQuestionInSeconds() * 1000;
        this.startTimeInMillis = lastTimestamp;
    }

    public synchronized QuestionToGameDto getCurrentQuestionDto() {
        while (!finished && (clock.millis() - lastTimestamp > timeForQuestionInMillis)) {
            doTimeout();
        }

        if (finished) {
            return null;
        }

        var dto = questionDtos.get(currentQuestionInd);
        dto.setNumber(currentQuestionInd);
        dto.setTimeLeft(getTimeLeftForCurrentQuestion());
        return dto;
    }

    public synchronized QuizQuestion getCurrentQuestion() {
        while (!finished && (clock.millis() - lastTimestamp > timeForQuestionInMillis)) {
            doTimeout();
        }

        return finished ? null : questionList.get(currentQuestionInd);
    }

    public synchronized UserAnswer doAnswer(String answer) {
        if (finished) {
            LOG.warn("Game process [{}] is finished, answer [{}] is ignored", soloGame.getId(), answer);
            return null;
        }

        if (clock.millis() - lastTimestamp > timeForQuestionInMillis) {
            return doTimeout();
        }

        lastTimestamp = clock.millis();

        UserAnswer.Status resultStatus = UserAnswer.Status.UNKNOWN;
        QuizQuestion currentQuestion = getCurrentQuestion();
        if (answer != null && answer.equals(currentQuestion.getCorrectAnswer())) {
            resultStatus = UserAnswer.Status.CORRECT;
        } else {
            for (WrongAnswer wrongAnswer : currentQuestion.getWrongAnswers()) {
                if (wrongAnswer.getValue().equals(answer)) {
                    resultStatus = UserAnswer.Status.WRONG;
                    break;
                }
            }
        }
        if (resultStatus == UserAnswer.Status.UNKNOWN) {
            LOG.warn("For gameprocess [{}] was given UNKNOWN answer {}",
                    getSoloGame().getId(), answer);
        }

        return addResult(currentQuestion, resultStatus, answer);
    }

    private UserAnswer doTimeout() {
        LOG.info("Timeout for question {} in game with id {}",
                currentQuestionInd,
                soloGame.getId());
        lastTimestamp += timeForQuestionInMillis;
        return addResult(questionList.get(currentQuestionInd), UserAnswer.Status.TIMEOUT, null);
    }

    private UserAnswer addResult(QuizQuestion quizQuestion, UserAnswer.Status status, String answer) {
        UserAnswer result = new UserAnswer();
        result.setQuizQuestion(quizQuestion);
        result.setStatus(status);
        result.setAnswer(answer);
        result.setGame(soloGame);

        userAnswers.add(result);
        if (userAnswers.size() == questionList.size()) {
            finishGame();
        }

        currentQuestionInd = userAnswers.size();

        return result;
    }

    public UserAnswer getLastAnswerResult() {
        return userAnswers.get(currentQuestionInd - 1);
    }

    private synchronized void finishGame() {
        finished = true;
    }

    public SoloGame getSoloGame() {
        return soloGame;
    }

    public int getCurrentQuestionInd() {
        return currentQuestionInd;
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public int getQuestionsCount() {
        return questionList.size();
    }

    public long getTimeForQuestionInMillis() {
        return timeForQuestionInMillis;
    }

    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    private long getTimeLeftForCurrentQuestion() {
        return lastTimestamp + timeForQuestionInMillis - clock.millis();
    }
}
