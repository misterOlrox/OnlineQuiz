package com.olrox.quiz.process;

import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.WrongAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SoloGameProcess {

    private volatile int currentQuestionInd;
    private SoloGame soloGame;
    private List<QuestionDto> questionDtos;
    private List<QuizQuestion> questionList;
    private List<AnswerResult> results;
    private volatile boolean finished = false;

    public SoloGameProcess(SoloGame soloGame) {
        this.soloGame = soloGame;
        this.questionList = soloGame.getQuestionList();
        this.questionDtos = questionList.stream().map(QuestionDto::new).collect(Collectors.toList());
        this.currentQuestionInd = 0;
        this.results = new ArrayList<>();
    }

    public synchronized QuestionDto getCurrentQuestionDto() {
        var dto = questionDtos.get(currentQuestionInd);
        dto.number = currentQuestionInd;
        return dto;
    }

    public synchronized QuizQuestion getCurrentQuestion() {
        return finished ? null : questionList.get(currentQuestionInd);
    }

    public synchronized AnswerResult doAnswer(String answer) {
        if (finished) {
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
        } else {
            currentQuestionInd = results.size();
        }

        return result;
    }

    private synchronized void finishGame() {
        finished = true;
        currentQuestionInd = -1;
    }

    public SoloGame getSoloGame() {
        return soloGame;
    }

    public int getCurrentQuestionInd() {
        return currentQuestionInd;
    }

    public static class QuestionDto {
        public int number;
        public String question;
        public List<String> answers = new ArrayList<>();

        public QuestionDto(QuizQuestion quizQuestion) {
            this.question = quizQuestion.getQuestion();

            answers.add(quizQuestion.getCorrectAnswer());
            quizQuestion.getWrongAnswers().forEach(x -> answers.add(x.getValue()));
            Collections.shuffle(answers);
        }
    }

    public synchronized boolean isFinished() {
        return finished;
    }
}
