package com.maryam.image.manager.api.service;

import com.maryam.image.manager.api.domain.ImageDetails;
import com.maryam.image.manager.api.domain.ImageMetadata;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ImageDetails uploadImage(@NotNull final MultipartFile file,
                             @NotNull final ImageMetadata imageMetadata,
                             @NotNull final String username,
                             final boolean isPublic);

    void deleteImage(@NotNull final String username, @NotNull final String imageId);

    void deleteImages(@NotNull final String username, @NotNull final List<String> imageIds);

    void deleteAllImages(@NotNull final String username);

    List<ImageDetails> getUserImages(@NotNull final String username, final boolean publicOnly);
}
