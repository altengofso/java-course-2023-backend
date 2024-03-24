package edu.java.scrapper.repository.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@IdClass(SubscriptionId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    @Id
    private long linkId;
    @Id
    private long chatId;
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    public SubscriptionDto(long linkId, long chatId) {
        this.linkId = linkId;
        this.chatId = chatId;
    }
}
