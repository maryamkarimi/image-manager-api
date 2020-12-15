package com.maryam.image.manager.api.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStoreService {

    String uploadImage(@NotNull final String imageId, @NotNull final MultipartFile imageFile);

    String generatePreSignedUrl(@NotNull final String imageId);

    void deleteImage(@NotNull final String imageId);

}
