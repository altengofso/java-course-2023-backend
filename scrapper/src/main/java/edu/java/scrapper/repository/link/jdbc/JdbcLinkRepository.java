package edu.java.scrapper.repository.link.jdbc;

import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.link.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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
    public LinkDto add(URI url) {
        return jdbcClient.sql("insert into link (url) values (?) on conflict do nothing returning *")
            .param(url)
            .query(LinkDto.class)
            .single();
    }

    @Override
    @Transactional
    public void remove(long linkId) {
        jdbcClient.sql("delete from link where id = ?")
            .param(linkId)
            .update();
    }

    @Override
    @Transactional
    public List<LinkDto> findAll() {
        return jdbcClient.sql("select * from link")
            .query(LinkDto.class)
            .list();
    }

    @Override
    @Transactional
    public List<LinkDto> findAllByChatId(long chatId) {
        return jdbcClient.sql("""
                select * from link
                join chat on link.id = chat.id
                where chat.id = ?""")
            .param(chatId)
            .query(LinkDto.class)
            .list();
    }

    @Override
    @Transactional
    public Optional<LinkDto> findByUrl(URI url) {
        return jdbcClient.sql("select * from link where url = ?")
            .param(url)
            .query(LinkDto.class)
            .optional();
    }

    @Override
    @Transactional
    public List<LinkDto> findByLastCheckAtLessThanOrNull(OffsetDateTime lastCheckAt) {
        return jdbcClient.sql("select * from link where last_check_at < ? or last_check_at is null")
            .param(lastCheckAt)
            .query(LinkDto.class)
            .list();
    }

    @Override
    @Transactional
    public LinkDto setUpdatedAtAndLastCheckAtById(long linkId, OffsetDateTime updatedAt, OffsetDateTime lastCheckAt) {
        return jdbcClient.sql("update link set updated_at = ?, last_check_at = ? where id = ? returning  *")
            .param(updatedAt)
            .param(lastCheckAt)
            .param(linkId)
            .query(LinkDto.class)
            .single();
    }
}
