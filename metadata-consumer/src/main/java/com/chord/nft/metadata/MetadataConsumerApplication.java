package com.chord.nft.metadata;

import com.chord.nft.metadata.model.Token;
import com.chord.nft.metadata.model.TokenMetadata;
import com.chord.nft.metadata.repository.TokenMetadataRepository;
import com.chord.nft.metadata.sender.RabbitMQSender;
import com.chord.nft.metadata.service.ImageService;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
public class MetadataConsumerApplication  implements CommandLineRunner {

	@Autowired
	RabbitMQSender rabbitMQSender;

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

	public void run1(String... args) throws Exception {
		Token token = new Token();
		token.setTokenId("testtokenid");
		token.setNftContractAddress("testnftcontractaddress");

		rabbitMQSender.send(token);

		try {
			String tokenURI = "https://ipfs.io/ipfs/QmWiQE65tmpYzcokCheQmng2DCM33DEhjXcPB6PanwpAZo/10";
			/*
			String image = imageService.getImageFromTokenURI(tokenURI);
			System.out.println("image : " + image);

			String file = imageService.download(image);
			System.out.println("downloaded image : " + file);
			imageService.upload(file);*/

			JSONObject metadata = imageService.getMetadataFromTokenURI(tokenURI);
			//System.out.println("Metadata : " + metadata);
			TokenMetadata tokenMetadata = new TokenMetadata();
			tokenMetadata.setTokenId("testtokenid");
			tokenMetadata.setAddress("testaddress");
			tokenMetadata.setAssetCDNUrl("testassetcdnurl");
			tokenMetadata.setMetadata(Document.parse(metadata.toString()));

			//tokenMetadataRepository.saveMetadata(tokenMetadata);
			//System.out.println("token metadata saved");

			TokenMetadata savedMetadata = tokenMetadataRepository.findTokenMetadata("testaddress","testtokenid");
			System.out.println("saved metadata : " + savedMetadata);
			// System.out.println(" metadata : " + savedMetadata.getMetadata().toJson());
		}catch (Exception e){
			System.out.println("exception while downloading file : " + e.getMessage());
		}
	}
}
