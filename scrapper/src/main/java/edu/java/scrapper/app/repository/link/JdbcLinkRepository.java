package edu.java.scrapper.app.repository.link;

import edu.java.scrapper.app.repository.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public void add(URI url, OffsetDateTime lastCheckedAt, OffsetDateTime updatedAt) {
        jdbcClient.sql("insert into link (url, last_check_at, updated_at) values (?, ?, ?)")
            .param(List.of(url, lastCheckedAt, updatedAt))
            .update();
    }

    @Override
    @Transactional
    public void remove(long linkId) {
        jdbcClient.sql("delete from link where id = :linkId")
            .param("linkId", linkId)
            .update();
    }

    @Override
    @Transactional
    public List<LinkDto> findAll() {
        return jdbcClient.sql("select * from link")
            .query(LinkDto.class)
            .list();
    }
}
