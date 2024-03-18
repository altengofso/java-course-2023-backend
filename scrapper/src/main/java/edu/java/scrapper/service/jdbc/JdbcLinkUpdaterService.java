package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdaterService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public int update(OffsetDateTime lastCheckAt) {
        OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime newLastCheckAt = OffsetDateTime.now(ZoneOffset.UTC);
        var links = linkRepository.findByLastCheckAtLessThanOrNull(lastCheckAt);
        links.forEach(linkDto -> linkRepository.setUpdatedAtAndLastCheckAtById(
            linkDto.id(),
            updatedAt,
            newLastCheckAt
        ));
        return links.size();
    }
}
