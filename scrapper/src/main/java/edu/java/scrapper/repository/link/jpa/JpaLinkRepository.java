package edu.java.scrapper.repository.link.jpa;

import edu.java.scrapper.repository.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLinkRepository extends JpaRepository<LinkDto, Long> {
    @Query(value = """
            select * from link
            join subscription on link.id = subscription.link_id
            where subscription.chat_id = :chatId""",
            nativeQuery = true)
    List<LinkDto> getAllLinks(@Param("chatId") long chatId);

    Optional<LinkDto> findByUrl(URI url);

    @Override
    void deleteById(Long aLong);

    List<LinkDto> findAllByLastCheckAtIsLessThanOrLastCheckAtIsNull(OffsetDateTime lastCheckAt);
}
