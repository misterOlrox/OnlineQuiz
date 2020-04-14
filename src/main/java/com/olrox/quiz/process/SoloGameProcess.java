package com.olrox.quiz.process;

import com.olrox.quiz.dto.QuestionDto;
import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SoloGameProcess {

    public static final Logger LOG = LoggerFactory.getLogger(SoloGameProcess.class);

    private volatile int currentQuestionInd;
    private SoloGame soloGame;
    private List<QuestionDto> questionDtos;
    private List<QuizQuestion> questionList;
    private List<AnswerResult> results;
    private volatile boolean finished = false;
    private User participant;

    public SoloGameProcess(SoloGame soloGame, User participant) {
        this.soloGame = soloGame;
        this.questionList = soloGame.getQuestionList();
        this.questionDtos = questionList.stream().map(QuestionDto::new).collect(Collectors.toList());
        this.currentQuestionInd = 0;
        this.results = new ArrayList<>();
        this.participant = participant;
    }

    public synchronized QuestionDto getCurrentQuestionDto() {
        if (finished) {
            return null;
        }
        var dto = questionDtos.get(currentQuestionInd);
        dto.setNumber(currentQuestionInd);
        return dto;
    }

    public synchronized QuizQuestion getCurrentQuestion() {
        return finished ? null : questionList.get(currentQuestionInd);
    }

    public synchronized AnswerResult doAnswer(String answer) {
        if (finished) {
            LOG.warn("Game process [{}] is finished, answer [{}] is ignored", soloGame.getId(), answer);
            return null;
        }

        AnswerResult result = new AnswerResult();
        AnswerResult.Status resultStatus = AnswerResult.Status.UNKNOWN;
        QuizQuestion currentQuestion = getCurrentQuestion();
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            resultStatus = AnswerResult.Status.CORRECT;
        } else {
            for (WrongAnswer wrongAnswer : currentQuestion.getWrongAnswers()) {
                if (wrongAnswer.getValue().equals(answer)) {
                    resultStatus = AnswerResult.Status.WRONG;
                    break;
                }
            }
        }

        result.setQuizQuestion(currentQuestion);
        result.setStatus(resultStatus);
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
}
