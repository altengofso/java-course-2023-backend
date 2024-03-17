package edu.java.scrapper.app.repository.link;

import edu.java.scrapper.app.repository.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    void add(URI url, OffsetDateTime lastCheckedAt, OffsetDateTime updatedAt);

    void remove(long linkId);

    List<LinkDto> findAll();
}
