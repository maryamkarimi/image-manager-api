package com.maryam.image.manager.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class ImageMetadata {

    @Nullable final private String title;
    @Nullable final private String description;
    @Nullable final private String location;
    @Nullable final private LocalDate uploadDate;

    @JsonCreator
    public ImageMetadata(@Nullable final String title,
                         @Nullable final String description,
                         @Nullable final String location) {

        this.title = title;
        this.description = description;
        this.location = location;
        this.uploadDate = LocalDate.now();
    }

    public ImageMetadata(@Nullable final String title,
                         @Nullable final String description,
                         @Nullable final String location,
                         @Nullable final LocalDate uploadDate) {

        this.title = title;
        this.description = description;
        this.location = location;
        this.uploadDate = uploadDate;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    @Nullable
    public LocalDate getUploadDate() {
        return uploadDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ImageMetadata that = (ImageMetadata) o;

        return new EqualsBuilder()
                .append(title, that.title)
                .append(description, that.description)
                .append(location, that.location)
                .append(uploadDate, that.uploadDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(description)
                .append(location)
                .append(uploadDate)
                .toHashCode();
    }

}
