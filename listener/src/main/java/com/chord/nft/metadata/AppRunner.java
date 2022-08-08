package com.chord.nft.metadata;

import com.chord.nft.metadata.message.RabbitMQSender;
import com.chord.nft.metadata.model.Token;
import com.chord.nft.metadata.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppRunner {
    @Autowired
    ImageService imageService;

    @Autowired
    RabbitMQSender rabbitMQSender;
    public void run() {
        try {
            /*
            String tokenURI = "https://ipfs.io/ipfs/QmWiQE65tmpYzcokCheQmng2DCM33DEhjXcPB6PanwpAZo/10";
            String image = imageService.getImageFromTokenURI(tokenURI);
            System.out.println("image : " + image);

            String file = imageService.download(image);
            System.out.println("downloaded image : " + file);
            imageService.upload(file);
            */

            Token token = new Token();
            token.setTokenId("testtokenid");
            token.setNftContractAddress("testnftcontractaddress");
            token.setTokenURI("https://ipfs.io/ipfs/QmWiQE65tmpYzcokCheQmng2DCM33DEhjXcPB6PanwpAZo/10");
            rabbitMQSender.send(token);
            System.out.println("Sent message into rabbitmq");
        }catch (Exception e){
            System.out.println("exception while downloading file : " + e.getMessage());
        }
    }
}
