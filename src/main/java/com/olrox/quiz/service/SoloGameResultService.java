package com.olrox.quiz.service;

import com.olrox.quiz.entity.AnswerResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoloGameResultService {
//    @Autowired
//    private SoloGameResultRepository resultRepository;
//
//    public SoloGameResult saveTotalResult(SoloGame parent, User participant, List<AnswerResult> answerResultList) {
//        var totalResult = new SoloGameResult();
//        totalResult.setParent(parent);
//        totalResult.setParticipant(participant);
//        totalResult.setCorrectAnswersCount(
//                (int) answerResultList.stream()
//                        .filter(x -> x.getStatus() == AnswerResult.Status.CORRECT)
//                        .count()
//        );
//
//        return resultRepository.save(totalResult);
//    }
//
//    public Optional<SoloGameResult> find(Long id) {
//        return resultRepository.findById(id);
//    }
//
//    public Long getResultIdBySoloGameId(Long soloGameId) {
//        return resultRepository.findSoloGameResultByParentId(soloGameId);
//    }

    public int countCorrectAnswers(List<AnswerResult> answerResultList) {
        return (int) answerResultList.stream()
                .filter(x -> x.getStatus() == AnswerResult.Status.CORRECT)
                .count();
    }
}
