package com.maryam.image.manager.api.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.maryam.image.manager.api.service.ImageStoreService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

@Service
public class AmazonS3Service implements ImageStoreService {

    @NotNull final AmazonS3 amazonS3Client;
    @NotNull final String bucketName;
    @NotNull final Integer urlExpireMinutes;

    public AmazonS3Service(@NotNull final AmazonS3 amazonS3Client,
                           @Value("${amazon.s3.bucketName}") @NotNull final String bucketName,
                           @Value("${amazon.s3.urlExpireMinutes}") @NotNull final Integer urlExpireTime) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
        this.urlExpireMinutes = urlExpireTime;
    }

    @Override
    public String uploadImage(@NotNull final String imageId, @NotNull final MultipartFile imageFile) {
        final ObjectMetadata imageMetadata = new ObjectMetadata();
        imageMetadata.setContentLength(imageFile.getSize());
        imageMetadata.setContentType(imageFile.getContentType());

        final PutObjectRequest putObjectRequest;

        try {
            putObjectRequest = new PutObjectRequest(bucketName, imageId, imageFile.getInputStream(), imageMetadata);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        amazonS3Client.putObject(putObjectRequest);
        return generatePreSignedUrl(imageId);
    }

    @Override
    public String generatePreSignedUrl(@NotNull final String imageId) {

        final GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, imageId)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(getExpirationTime());

        final URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private Date getExpirationTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, urlExpireMinutes);
        return calendar.getTime();
    }

    @Override
    public void deleteImage(@NotNull final String imageId) {
        amazonS3Client.deleteObject(bucketName, imageId);
    }

}
