package com.maryam.image.manager.api.service.impl;

import com.maryam.image.manager.api.service.ImageIdGeneratorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageIdGeneratorServiceImpl implements ImageIdGeneratorService {

    @Override
    public String generateUniqueImageId() {
        return UUID.randomUUID().toString();
    }

}
