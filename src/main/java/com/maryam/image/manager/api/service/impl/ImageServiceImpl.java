package com.maryam.image.manager.api.service.impl;

import com.maryam.image.manager.api.domain.ImageDetails;
import com.maryam.image.manager.api.domain.ImageMetadata;
import com.maryam.image.manager.api.domain.cassandra.UserImage;
import com.maryam.image.manager.api.domain.cassandra.UserImageKey;
import com.maryam.image.manager.api.repository.ImageRepository;
import com.maryam.image.manager.api.service.ImageIdGeneratorService;
import com.maryam.image.manager.api.service.ImageService;
import com.maryam.image.manager.api.service.ImageStoreService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @NotNull final ImageStoreService imageStoreService;
    @NotNull final ImageRepository imageRepository;
    @NotNull final ImageIdGeneratorService imageIdGeneratorService;

    public ImageServiceImpl(@NotNull final ImageStoreService imageStoreService,
                            @NotNull final ImageRepository imageRepository,
                            @NotNull final ImageIdGeneratorService imageIdGeneratorService) {
        this.imageStoreService = imageStoreService;
        this.imageRepository = imageRepository;
        this.imageIdGeneratorService = imageIdGeneratorService;
    }

    @Override
    public ImageDetails uploadImage(@NotNull final MultipartFile imageFile,
                                    @NotNull final ImageMetadata imageMetadata,
                                    @NotNull final String username,
                                    final boolean isPublic) {

        final String imageId = imageIdGeneratorService.generateUniqueImageId();
        final String imageUrl = imageStoreService.uploadImage(imageId, imageFile);

        final UserImage userImage = new UserImage(new UserImageKey(username, imageId),
                                                  isPublic,
                                                  imageMetadata.getTitle(),
                                                  imageMetadata.getDescription(),
                                                  imageMetadata.getLocation(),
                                                  imageMetadata.getUploadDate());

        imageRepository.save(userImage);

        return new ImageDetails(username, imageId, imageMetadata, imageUrl);
    }

    @Override
    public void deleteImage(@NotNull final String username, @NotNull final String imageId) {
        final boolean imageDeleted = imageRepository.deleteUserImage(username, imageId);
        if (imageDeleted) {
            imageStoreService.deleteImage(imageId);
        }
    }

    @Override
    public void deleteImages(@NotNull final String username, @NotNull final List<String> imageIds) {
        imageIds.forEach(imageId -> deleteImage(username, imageId));
    }

    @Override
    public void deleteAllImages(@NotNull final String username) {
        final List<String> imagesToDelete = imageRepository.getUserImageKeys(username)
                                                           .stream()
                                                           .map(userImage -> userImage.getUserImageKey().getImageId())
                                                           .collect(Collectors.toList());

        imageRepository.deleteAllUserImages(username);
        imagesToDelete.forEach(imageStoreService::deleteImage);
    }

    @Override
    public List<ImageDetails> getUserImages(@NotNull final String username, final boolean publicOnly) {
        final List<UserImage> userImages = publicOnly ? imageRepository.getPublicImages(username) :
                                                        imageRepository.getUserImages(username);
        return userImages.stream()
                         .map(this::convertUserImageToImageDetails)
                         .collect(Collectors.toList());
    }

    private ImageDetails convertUserImageToImageDetails(@NotNull final UserImage userImage) {
        final String imageId = Objects.requireNonNull(userImage.getUserImageKey().getImageId());
        final String imageUrl = imageStoreService.generatePreSignedUrl(imageId);

        return new ImageDetails(userImage.getUserImageKey().getUsername(),
                                userImage.getUserImageKey().getImageId(),
                                createImageMetadata(userImage),
                                imageUrl);
    }

    private static ImageMetadata createImageMetadata(@NotNull final UserImage userImage) {
        return new ImageMetadata(userImage.getTitle(),
                                 userImage.getDescription(),
                                 userImage.getLocation(),
                                 userImage.getUploadDate());
    }

}
