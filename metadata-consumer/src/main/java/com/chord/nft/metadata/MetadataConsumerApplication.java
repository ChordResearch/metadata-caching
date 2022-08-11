package com.chord.nft.metadata;

import com.chord.nft.metadata.repository.TokenMetadataRepository;
import com.chord.nft.metadata.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetadataConsumerApplication implements CommandLineRunner {


    @Autowired
    ImageService imageService;

    @Autowired
    TokenMetadataRepository tokenMetadataRepository;

    public static void main(String[] args) {
        SpringApplication.run(MetadataConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Metadata-consumer started");
    }

}
