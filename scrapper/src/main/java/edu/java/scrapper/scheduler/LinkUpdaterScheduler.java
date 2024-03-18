package edu.java.scrapper.scheduler;

import edu.java.scrapper.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdaterService linkUpdaterService;

    @Value("${app.scheduler.check-delay}")
    private long checkDelay;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        OffsetDateTime lastCheckAt = OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(checkDelay);
        linkUpdaterService.update(lastCheckAt);
    }
}
