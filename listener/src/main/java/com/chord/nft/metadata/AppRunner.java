package com.chord.nft.metadata;

import com.chord.nft.metadata.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppRunner {
    @Autowired
    ImageService imageService;

    public void run() {
        imageService.save();
    }
}
