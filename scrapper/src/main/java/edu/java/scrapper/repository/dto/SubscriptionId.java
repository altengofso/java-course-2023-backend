package edu.java.scrapper.repository.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionId implements Serializable {
    private long linkId;
    private long chatId;
}
