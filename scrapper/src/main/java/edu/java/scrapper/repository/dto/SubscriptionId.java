package edu.java.scrapper.repository.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SubscriptionId implements Serializable {
    private final long linkId;
    private final long chatId;
}
