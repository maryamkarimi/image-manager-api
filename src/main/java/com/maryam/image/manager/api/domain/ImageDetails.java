package com.maryam.image.manager.api.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

public class ImageDetails {

    @NotNull private final String username;
    @NotNull private final String imageId;
    @NotNull private final ImageMetadata imageMetadata;
    @NotNull private final String imageUrl;

    public ImageDetails(@NotNull final String username,
                        @NotNull final String imageId,
                        @NotNull final ImageMetadata imageMetadata,
                        @NotNull final String imageUrl) {
        this.username = username;
        this.imageId = imageId;
        this.imageMetadata = imageMetadata;
        this.imageUrl = imageUrl;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getImageId() {
        return imageId;
    }

    @NotNull
    public ImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    @NotNull
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ImageDetails that = (ImageDetails) o;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(imageId, that.imageId)
                .append(imageMetadata, that.imageMetadata)
                .append(imageUrl, that.imageUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(imageId)
                .append(imageMetadata)
                .append(imageUrl)
                .toHashCode();
    }

}
