package edu.java.scrapper.repository.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    @Id
    private long id;
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    public ChatDto(long id) {
        this.id = id;
    }
}
