package com.olrox.quiz.service;

import com.olrox.quiz.entity.AnswerResult;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.SoloGameResult;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.SoloGameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoloGameResultService {
    @Autowired
    private SoloGameResultRepository resultRepository;

    public SoloGameResult saveTotalResult(SoloGame parent, User participant, List<AnswerResult> answerResultList) {
        var totalResult = new SoloGameResult();
        totalResult.setParent(parent);
        totalResult.setParticipant(participant);
        totalResult.setCorrectAnswersCount(
                (int) answerResultList.stream()
                        .filter(x -> x.getStatus() == AnswerResult.Status.CORRECT)
                        .count()
        );

        return resultRepository.save(totalResult);
    }

    public Optional<SoloGameResult> find(Long id) {
        return resultRepository.findById(id);
    }

}
