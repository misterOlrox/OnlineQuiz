package com.olrox.quiz.rest;

import com.olrox.quiz.dto.AnswerDto;
import com.olrox.quiz.dto.AnswerResultDto;
import com.olrox.quiz.dto.ErrorDto;
import com.olrox.quiz.dto.NextQuestionAndPrevResultDto;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.service.SoloGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SoloGameRestController {

    public static final Logger LOG = LoggerFactory.getLogger(SoloGameRestController.class);

    @Autowired
    private SoloGameService soloGameService;

    @GetMapping("/game/solo/{id}/question")
    public ResponseEntity<?> getQuestion(
            @PathVariable Long id, @AuthenticationPrincipal User user) {

        var process = soloGameService.getGameProcessById(id);

        var validationFailedResponse = validationFailedResponse(process, user);
        if (validationFailedResponse.isPresent()) {
            return validationFailedResponse.get();
        }

        return new ResponseEntity<>(process.getCurrentQuestionDto(), HttpStatus.OK);
    }

    @GetMapping("/game/solo/{id}/result")
    public ResponseEntity<?> getLastResultAnswer(
            @PathVariable Long id, @AuthenticationPrincipal User user) {

        var process = soloGameService.getGameProcessById(id);

        var validationFailedResponse = validationFailedResponse(process, user);
        if (validationFailedResponse.isPresent()) {
            return validationFailedResponse.get();
        }

        var result = process.getLastAnswerResult();
        var response = new NextQuestionAndPrevResultDto();
        response.setNextQuestion(process.getCurrentQuestionDto());
        response.setPrevResult(AnswerResultDto.from(result));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/game/solo/{id}/answer")
    public ResponseEntity<?> postAnswer(
            @PathVariable Long id,
            @RequestBody AnswerDto answerDto,
            @AuthenticationPrincipal User user
    ) {
        LOG.info("Do answer for game with id {}, answer is {}", id, answerDto.getValue());
        var process = soloGameService.getGameProcessById(id);
        if (process == null) {
            SoloGame finishedGame = soloGameService.findFinishedGame(id).orElse(null);
            if (finishedGame != null) {
                var response = new NextQuestionAndPrevResultDto();
                response.setEnded(true);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ErrorDto("Game process doesn't exists"),
                        HttpStatus.NOT_FOUND);
            }
        }

        var userIsNotParticipant = userIsNotParticipantResponse(process, user);
        if (userIsNotParticipant.isPresent()) {
            return userIsNotParticipant.get();
        }

        var result = process.doAnswer(answerDto.getValue());
        if (result == null) {
            return new ResponseEntity<>(new NextQuestionAndPrevResultDto(), HttpStatus.OK);
        }

        var answerResultDto = AnswerResultDto.from(result);
        LOG.info("Answer result for game with id {}, answer is {}", id, answerResultDto);
        var response = new NextQuestionAndPrevResultDto();
        response.setNextQuestion(process.getCurrentQuestionDto());
        response.setPrevResult(answerResultDto);

        if (process.isFinished()) {
            soloGameService.finishGame(process);
            response.setEnded(true);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Optional<ResponseEntity<?>> validationFailedResponse(SoloGameProcess process, User user) {
        if (process == null) {
            LOG.warn("User [{}] tried to get game process that doesn't exist", user.getUsername());
            return Optional.of(new ResponseEntity<>(
                    new ErrorDto("Game process doesn't exists"),
                    HttpStatus.NOT_FOUND)
            );
        }

        return userIsNotParticipantResponse(process, user);
    }

    private Optional<ResponseEntity<?>> userIsNotParticipantResponse(SoloGameProcess process, User user) {
        if (!user.equals(process.getSoloGame().getParticipant())) {
            LOG.warn("User [{}] tried to get not his game process", user.getUsername());
            return Optional.of(new ResponseEntity<>(
                    new ErrorDto("This ins't your game!"),
                    HttpStatus.UNAUTHORIZED)
            );
        }

        return Optional.empty();
    }
}
