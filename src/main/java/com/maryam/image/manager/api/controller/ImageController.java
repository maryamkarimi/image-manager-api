package com.maryam.image.manager.api.controller;

import com.maryam.image.manager.api.domain.ImageDetails;
import com.maryam.image.manager.api.domain.ImageMetadata;
import com.maryam.image.manager.api.service.ImageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ImageController {

    @NotNull final ImageService imageService;

    public ImageController(@NotNull final ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload-images")
    public List<ImageDetails> uploadImages(@RequestParam("images") @NotNull final MultipartFile[] imageFiles,
                                           @RequestParam("metadata") @NotNull final ImageMetadata imageMetadata,
                                           @RequestParam("public") final boolean isPublic,
                                           @NotNull final Authentication authentication) {

        return Stream.of(imageFiles)
                     .map(imageFile -> imageService.uploadImage(imageFile, imageMetadata, authentication.getName(), isPublic))
                     .collect(Collectors.toList());
    }

    @DeleteMapping("delete-image/{imageId}")
    public void deleteImage(@PathVariable(name = "imageId") @NotNull final String imageId,
                            @NotNull final Authentication authentication) {
        imageService.deleteImage(authentication.getName(), imageId);
    }

    @DeleteMapping("delete-images")
    public void deleteImages(@RequestBody @NotNull final List<String> imageIds,
                             @NotNull final Authentication authentication) {
        imageService.deleteImages(authentication.getName(), imageIds);
    }

    @DeleteMapping("delete-all-images")
    public void deleteAllImages(@NotNull final Authentication authentication) {
        imageService.deleteAllImages(authentication.getName());
    }

    @GetMapping("images")
    public List<ImageDetails> getUserImages(@NotNull final Authentication authentication) {
        return imageService.getUserImages(authentication.getName(), false);
    }

    @GetMapping("images/{username}")
    public List<ImageDetails> getUserImages(@PathVariable(name = "username") @NotNull final String username) {
        return imageService.getUserImages(username, true);
    }

}
