package edu.java.scrapper.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;

@Converter
public class URIConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return uri != null ? uri.toString() : null;
    }

    @Override
    public URI convertToEntityAttribute(String s) {
        return s != null ? URI.create(s) : null;
    }
}
