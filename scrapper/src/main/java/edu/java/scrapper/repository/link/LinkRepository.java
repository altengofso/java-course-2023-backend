package edu.java.scrapper.repository.link;

import edu.java.scrapper.repository.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    LinkDto add(URI url);

    LinkDto remove(long linkId);

    List<LinkDto> findAll();

    List<LinkDto> findAllByChatId(long chatId);

    Optional<LinkDto> findByUrl(URI url);

    List<LinkDto> findByLastCheckAtLessThanOrNull(OffsetDateTime lastCheckAt);

    LinkDto setUpdatedAtAndLastCheckAtById(long linkId, OffsetDateTime updatedAt, OffsetDateTime lastCheckAt);
}
