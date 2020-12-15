package com.maryam.image.manager.api.domain.cassandra;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table("image_by_user")
public class UserImage {

    @NotNull
    @PrimaryKey
    final private UserImageKey userImageKey;

    @NotNull
    @Column("image_is_public")
    final private Boolean isPublic;

    @Nullable
    @Column("image_title")
    final private String title;

    @Nullable
    @Column("image_description")
    final private String description;

    @Nullable
    @Column("image_location")
    final private String location;

    @Nullable
    @Column("image_upload_date")
    @CassandraType(type = CassandraType.Name.DATE)
    final private LocalDate uploadDate;

    public UserImage(@NotNull final UserImageKey userImageKey,
                     @NotNull final Boolean isPublic,
                     @Nullable final String title,
                     @Nullable final String description,
                     @Nullable final String location,
                     @Nullable final LocalDate uploadDate) {
        this.userImageKey = userImageKey;
        this.isPublic = isPublic;
        this.title = title;
        this.description = description;
        this.location = location;
        this.uploadDate = uploadDate;
    }

    @NotNull
    public UserImageKey getUserImageKey() {
        return userImageKey;
    }

    @NotNull
    public Boolean getPublic() {
        return isPublic;
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
}
