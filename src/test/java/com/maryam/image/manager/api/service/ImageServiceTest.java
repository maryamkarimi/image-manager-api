package com.maryam.image.manager.api.service;

import com.maryam.image.manager.api.domain.ImageDetails;
import com.maryam.image.manager.api.domain.ImageMetadata;
import com.maryam.image.manager.api.domain.cassandra.UserImage;
import com.maryam.image.manager.api.domain.cassandra.UserImageKey;
import com.maryam.image.manager.api.repository.ImageRepository;
import com.maryam.image.manager.api.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ImageServiceTest {

    private static final String DEFAULT_USERNAME = "user1";
    private static final String VALID_IMAGE_ID = "validImageId";
    private static final String IMAGE_ID_NOT_BELONGING_TO_USER = "invalidImageId";
    private static final UserImage DEFAULT_USER_IMAGE  = new UserImage(new UserImageKey(DEFAULT_USERNAME, VALID_IMAGE_ID),
                                                                       true,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null);

    private final ImageStoreService imageStoreService = Mockito.mock(ImageStoreService.class);
    private final ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
    private final ImageIdGeneratorService imageIdGeneratorService = Mockito.mock(ImageIdGeneratorService.class);

    private final ImageService imageService = new ImageServiceImpl(imageStoreService, imageRepository, imageIdGeneratorService);

    @Test
    void testUploadImageSavesToStorageAndDatabase() {

        final MultipartFile imageFile = Mockito.mock(MultipartFile.class);
        final ImageMetadata imageMetadata = new ImageMetadata(null, null, null);
        final String imageUrl = "ImageUrl";
        when(imageIdGeneratorService.generateUniqueImageId()).thenReturn(VALID_IMAGE_ID);
        when(imageStoreService.uploadImage(VALID_IMAGE_ID, imageFile)).thenReturn(imageUrl);

        final ImageDetails imageDetails = imageService.uploadImage(imageFile, imageMetadata, DEFAULT_USERNAME, true);

        verify(imageStoreService, times(1)).uploadImage(any(), any());
        verify(imageRepository, times(1)).save(any());
        assertEquals(imageDetails.getImageUrl(), imageUrl);
    }

    @Test
    void testDeleteImageDeletesFromStorageAndDatabase() {
        when(imageRepository.deleteUserImage(DEFAULT_USERNAME, VALID_IMAGE_ID)).thenReturn(true);

        imageService.deleteImage(DEFAULT_USERNAME, VALID_IMAGE_ID);

        verify(imageStoreService, times(1)).deleteImage(VALID_IMAGE_ID);
        verify(imageRepository, times(1)).deleteUserImage(DEFAULT_USERNAME, VALID_IMAGE_ID);
    }

    @Test
    void testDeleteImagesDoesntDeleteImageNotBelongingToUser() {
        when(imageRepository.deleteUserImage(DEFAULT_USERNAME, VALID_IMAGE_ID)).thenReturn(true);
        when(imageRepository.deleteUserImage(DEFAULT_USERNAME, IMAGE_ID_NOT_BELONGING_TO_USER)).thenReturn(false);

        imageService.deleteImages(DEFAULT_USERNAME, Arrays.asList(VALID_IMAGE_ID, IMAGE_ID_NOT_BELONGING_TO_USER));

        verify(imageStoreService, times(1)).deleteImage(VALID_IMAGE_ID);
        verify(imageStoreService, times(0)).deleteImage(IMAGE_ID_NOT_BELONGING_TO_USER);
    }

    @Test
    void testDeleteAllImagesDeletesFromStorageAndDatabase() {
        when(imageRepository.getUserImageKeys(DEFAULT_USERNAME)).thenReturn(Collections.singletonList(DEFAULT_USER_IMAGE));

        imageService.deleteAllImages(DEFAULT_USERNAME);

        verify(imageRepository, times(1)).deleteAllUserImages(DEFAULT_USERNAME);
        verify(imageStoreService, times(1)).deleteImage(VALID_IMAGE_ID);
    }

    @Test
    void testGetUserImagesPublicOnly() {
        final String imageUrl = "ImageUrl";
        final ImageDetails expectedImageDetails = createImageDetails(imageUrl,
                                                                     DEFAULT_USER_IMAGE.getUserImageKey().getUsername(),
                                                                     DEFAULT_USER_IMAGE.getUserImageKey().getImageId(),
                                                                     new ImageMetadata(null, null, null, null));

        when(imageStoreService.generatePreSignedUrl(DEFAULT_USER_IMAGE.getUserImageKey().getImageId())).thenReturn(imageUrl);
        when(imageRepository.getPublicImages(DEFAULT_USERNAME)).thenReturn(Collections.singletonList(DEFAULT_USER_IMAGE));

        final List<ImageDetails> imageDetails = imageService.getUserImages(DEFAULT_USER_IMAGE.getUserImageKey().getUsername(), true);

        verify(imageRepository, times(1)).getPublicImages(DEFAULT_USERNAME);
        verify(imageRepository, times(0)).getUserImages(DEFAULT_USERNAME);
        assertEquals(Collections.singletonList(expectedImageDetails), imageDetails);
    }

    @Test
    void testGetUserImagesAll() {
        final String imageUrl = "ImageUrl";
        final ImageDetails expectedImageDetails = createImageDetails(imageUrl,
                                                                     DEFAULT_USER_IMAGE.getUserImageKey().getUsername(),
                                                                     DEFAULT_USER_IMAGE.getUserImageKey().getImageId(),
                                                                     new ImageMetadata(null, null, null, null));

        when(imageStoreService.generatePreSignedUrl(DEFAULT_USER_IMAGE.getUserImageKey().getImageId())).thenReturn(imageUrl);
        when(imageRepository.getUserImages(DEFAULT_USERNAME)).thenReturn(Collections.singletonList(DEFAULT_USER_IMAGE));

        final List<ImageDetails> imageDetails = imageService.getUserImages(DEFAULT_USER_IMAGE.getUserImageKey().getUsername(), false);

        verify(imageRepository, times(1)).getUserImages(DEFAULT_USERNAME);
        verify(imageRepository, times(0)).getPublicImages(DEFAULT_USERNAME);
        assertEquals(Collections.singletonList(expectedImageDetails), imageDetails);
    }

    private ImageDetails createImageDetails(final String imageUrl,
                                            final String username,
                                            final String imageId,
                                            final ImageMetadata imageMetadata) {
        return new ImageDetails(username, imageId, imageMetadata, imageUrl);
    }

}