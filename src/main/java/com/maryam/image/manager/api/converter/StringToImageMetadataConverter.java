package com.maryam.image.manager.api.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.maryam.image.manager.api.domain.ImageMetadata;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToImageMetadataConverter implements Converter<String, ImageMetadata> {

    @Override
    public ImageMetadata convert(@NotNull final String input) {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        try {
            return mapper.readValue(input, ImageMetadata.class);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
