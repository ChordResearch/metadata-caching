package com.chord.nft.metadata;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetadataConsumerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MetadataConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Metadata-consumer started");
    }

}
