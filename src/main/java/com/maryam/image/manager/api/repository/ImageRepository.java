package com.maryam.image.manager.api.repository;

import com.maryam.image.manager.api.domain.cassandra.UserImage;
import com.maryam.image.manager.api.domain.cassandra.UserImageKey;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends CassandraRepository<UserImage, UserImageKey> {

    @Query("SELECT * FROM image_repository.image_by_user WHERE user_id = :username AND image_is_public = true ALLOW FILTERING")
    List<UserImage> getPublicImages(@Param("username") @NotNull final String username);

    @Query("SELECT * FROM image_repository.image_by_user WHERE user_id = :username")
    List<UserImage> getUserImages(@Param("username") @NotNull final String username);

    @Query("SELECT user_id, image_id, image_is_public FROM image_repository.image_by_user WHERE user_id = :username")
    List<UserImage> getUserImageKeys(@Param("username") @NotNull final String username);

    @Query("DELETE FROM image_repository.image_by_user WHERE user_id = :username AND image_id = :imageId IF EXISTS")
    boolean deleteUserImage(@Param("username") @NotNull final String username, @Param("imageId") @NotNull final String imageIds);

    @Query("DELETE FROM image_repository.image_by_user WHERE user_id = :username")
    void deleteAllUserImages(@Param("username") @NotNull final String username);

}
