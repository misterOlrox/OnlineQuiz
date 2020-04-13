package com.olrox.quiz.process;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.WrongAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SoloGameProcess {

    private static final Long CORRECT_ANSWER_CODE = -1L;
    public static final Long TIMEOUT_CODE = -2L;

    private volatile int currentQuestionInd;
    private SoloGame soloGame;
    private List<QuestionDto> questionDtos;
    private List<QuizQuestion> questionList;
    private Map<QuizQuestion, Long> answers;

    public SoloGameProcess(SoloGame soloGame) {
        this.soloGame = soloGame;
        this.questionList = soloGame.getQuestionList();
        this.questionDtos = questionList.stream().map(QuestionDto::new).collect(Collectors.toList());
        this.currentQuestionInd = 0;
        this.answers = new HashMap<>();
    }

    public synchronized QuestionDto getCurrentQuestionDto() {
        var dto = questionDtos.get(currentQuestionInd);
        dto.number = currentQuestionInd;
        return dto;
    }

    public synchronized QuizQuestion getCurrentQuestion() {
        return questionList.get(currentQuestionInd);
    }

    public synchronized boolean doAnswer(String answer) {
        currentQuestionInd++;
        QuizQuestion currentQuestion = getCurrentQuestion();
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            answers.put(currentQuestion, CORRECT_ANSWER_CODE);
            return true;
        } else {
            for (WrongAnswer wrongAnswer : currentQuestion.getWrongAnswers()) {
                if (wrongAnswer.getValue().equals(answer)) {
                    answers.put(currentQuestion, wrongAnswer.getId());
                    return false;
                }
            }

            answers.put(currentQuestion, TIMEOUT_CODE);
            return false;
        }
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
}
