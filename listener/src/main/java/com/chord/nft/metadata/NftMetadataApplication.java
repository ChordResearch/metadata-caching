package com.chord.nft.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NftMetadataApplication implements CommandLineRunner {

	@Autowired
	AppRunner appRunner;

	public static void main(String[] args) {
		SpringApplication.run(NftMetadataApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		appRunner.run();
	}
}
