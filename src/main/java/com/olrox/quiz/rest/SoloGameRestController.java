package com.olrox.quiz.rest;

import com.olrox.quiz.dto.AnswerDto;
import com.olrox.quiz.dto.AnswerResultDto;
import com.olrox.quiz.dto.NextQuestionAndPrevResultDto;
import com.olrox.quiz.dto.QuestionDto;
import com.olrox.quiz.service.SoloGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoloGameRestController {

    public static final Logger LOG = LoggerFactory.getLogger(SoloGameRestController.class);

    @Autowired
    private SoloGameService soloGameService;

    @GetMapping("/game/solo/{id}/question")
    public QuestionDto getQuestion(@PathVariable Long id) {
        var process = soloGameService.getGameProcessById(id);

        return process.getCurrentQuestionDto();
    }

    // TODO
    @PostMapping("/game/solo/{id}/answer")
    public NextQuestionAndPrevResultDto postAnswer(@PathVariable Long id, @RequestBody AnswerDto answerDto) {
        LOG.info("Do answer for game with id {}, answer is {}", id, answerDto.getValue());
        var process = soloGameService.getGameProcessById(id);
        var result = process.doAnswer(answerDto.getValue());
        if (result == null) {
            return new NextQuestionAndPrevResultDto();
        }
        var answerResultDto = AnswerResultDto.from(result);
        LOG.info("Answer result for game with id {}, answer is {}", id, answerResultDto);

        var response = new NextQuestionAndPrevResultDto();
        response.setNextQuestion(process.getCurrentQuestionDto());
        response.setPrevResult(answerResultDto);

        if (process.isFinished()) {
            soloGameService.finishGame(process);
        }

        return response;
    }
}
