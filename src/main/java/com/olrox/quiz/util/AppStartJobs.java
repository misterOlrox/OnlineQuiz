package com.olrox.quiz.util;

import com.olrox.quiz.service.SoloGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppStartJobs {

    private static final Logger LOG = LoggerFactory.getLogger(AppStartJobs.class);

    @Autowired
    private SoloGameService soloGameService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void clearGamesInProgressFromDb() {
        LOG.info("Deleting all games which were in progress");
        soloGameService.deleteGamesInProgress();
    }
}
