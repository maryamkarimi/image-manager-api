package com.maryam.image.manager.api.domain.cassandra;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class UserImageKey {

    @NotNull
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    final private String username;

    @NotNull
    @PrimaryKeyColumn(name = "image_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private final String imageId;

    @JsonCreator
    public UserImageKey(@NotNull final String username, @NotNull final String imageId) {
        this.username = username;
        this.imageId = imageId;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getImageId() {
        return imageId;
    }
}
