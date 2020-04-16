package com.olrox.quiz.process;

import com.olrox.quiz.dto.QuestionDto;
import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SoloGameProcess {

    public static final Logger LOG = LoggerFactory.getLogger(SoloGameProcess.class);

    private final SoloGame soloGame;
    private final List<QuestionDto> questionDtos;
    private final List<QuizQuestion> questionList;
    private final List<AnswerResult> results;
    private final User participant;
    private final Clock clock;
    private final long timeForQuestionInMillis;
    private final long startTimeInMillis;

    private volatile int currentQuestionInd;
    private volatile boolean finished = false;
    private volatile long lastTimestamp;

    public SoloGameProcess(SoloGame soloGame, User participant) {
        this(soloGame, participant, Clock.systemDefaultZone());
    }

    protected SoloGameProcess(SoloGame soloGame, User participant, Clock clock) {
        this.soloGame = soloGame;
        this.questionList = soloGame.getQuestionList();
        this.questionDtos = questionList.stream().map(QuestionDto::new).collect(Collectors.toList());
        this.currentQuestionInd = 0;
        this.results = new ArrayList<>();
        this.participant = participant;
        this.clock = clock;
        this.lastTimestamp = this.clock.millis();
        this.timeForQuestionInMillis = soloGame.getTimeForQuestionInSeconds() * 1000;
        this.startTimeInMillis = lastTimestamp;
    }

    public synchronized QuestionDto getCurrentQuestionDto() {
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

    public synchronized AnswerResult doAnswer(String answer) {
        if (finished) {
            LOG.warn("Game process [{}] is finished, answer [{}] is ignored", soloGame.getId(), answer);
            return null;
        }

        if (clock.millis() - lastTimestamp > timeForQuestionInMillis) {
            return doTimeout();
        }

        lastTimestamp = clock.millis();

        AnswerResult.Status resultStatus = AnswerResult.Status.UNKNOWN;
        QuizQuestion currentQuestion = getCurrentQuestion();
        if (answer != null && answer.equals(currentQuestion.getCorrectAnswer())) {
            resultStatus = AnswerResult.Status.CORRECT;
        } else {
            for (WrongAnswer wrongAnswer : currentQuestion.getWrongAnswers()) {
                if (wrongAnswer.getValue().equals(answer)) {
                    resultStatus = AnswerResult.Status.WRONG;
                    break;
                }
            }
        }

        return addResult(currentQuestion, resultStatus, answer);
    }

    private AnswerResult doTimeout() {
        LOG.info("Timeout for question {} in game with id {}",
                currentQuestionInd,
                soloGame.getId());
        lastTimestamp += timeForQuestionInMillis;
        return addResult(questionList.get(currentQuestionInd), AnswerResult.Status.TIMEOUT, null);
    }

    private AnswerResult addResult(QuizQuestion quizQuestion, AnswerResult.Status status, String answer) {
        AnswerResult result = new AnswerResult();
        result.setQuizQuestion(quizQuestion);
        result.setStatus(status);
        result.setAnswer(answer);

        results.add(result);
        if (results.size() == questionList.size()) {
            finishGame();
        }

        currentQuestionInd = results.size();

        return result;
    }

    public AnswerResult getLastAnswerResult() {
        return results.get(currentQuestionInd - 1);
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

    public List<AnswerResult> getResults() {
        return results;
    }

    public User getParticipant() {
        return participant;
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

    public synchronized Pair<Integer, Long> getCurrentIndAndNextTimeout() {
        return Pair.of(currentQuestionInd, getTimeForQuestionInMillis());
    }
}
