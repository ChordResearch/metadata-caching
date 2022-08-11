package com.chord.nft.metadata.consumer;

import com.chord.nft.metadata.model.Token;
import com.chord.nft.metadata.model.TokenMetadata;
import com.chord.nft.metadata.repository.TokenMetadataRepository;
import com.chord.nft.metadata.service.ImageService;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer {
    @Autowired
    ImageService imageService;

    @Autowired
    TokenMetadataRepository tokenMetadataRepository;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void recievedMessage(Token token) {
        try {
            System.out.println("Recieved Message From RabbitMQ: " + token);
            JSONObject metadata = imageService.getMetadataFromTokenURI(token.getTokenURI());
            String image = imageService.getImageFromTokenURI(token.getTokenURI());
            System.out.println("image : " + image);

            String file = imageService.download(image);
            System.out.println("downloaded image : " + file);
            String imageCDNPath = imageService.upload(file);
            System.out.println("image path in CDN " + imageCDNPath);
            TokenMetadata tokenMetadata = new TokenMetadata();
            tokenMetadata.setTokenId(token.getTokenId());
            tokenMetadata.setAddress(token.getNftContractAddress());
            tokenMetadata.setAssetCDNUrl(imageCDNPath);
            tokenMetadata.setMetadata(Document.parse(metadata.toString()));
            // saving metadata
            tokenMetadataRepository.saveMetadata(tokenMetadata);
        }catch (Exception e){

        }
    }
}