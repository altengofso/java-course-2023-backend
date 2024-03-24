package edu.java.scrapper.repository.dto;

import edu.java.scrapper.repository.converter.URIConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "link")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    @Id
    private long id;
    @Convert(converter = URIConverter.class)
    private URI url;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime updatedAt;
}
