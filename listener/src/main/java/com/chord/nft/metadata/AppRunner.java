package com.chord.nft.metadata;

import com.chord.nft.metadata.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppRunner {
    @Autowired
    ImageService imageService;

    public void run() {
        try {
            String file = imageService.download("https://sample-videos.com/img/Sample-png-image-100kb.png");
            imageService.upload(file);
        }catch (Exception e){
            System.out.println("exception while downloading file : " + e.getMessage());
        }
    }
}
