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

import java.util.Base64;


@Component
public class RabbitMQConsumer {
    @Autowired
    ImageService imageService;

    @Autowired
    TokenMetadataRepository tokenMetadataRepository;

    private String formatIpfsURL(String url) {
        return url.replaceAll("ipfs://", "https://ipfs.io/ipfs/");
    }

    private String downloadImageAndUploadToCDN(String fileURL) throws Exception{
        String file = imageService.download(fileURL);
        System.out.println("downloaded image : " + file);
        return imageService.upload(file);
    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receivedMessage(Token token) {
        TokenMetadata tokenMetadata = new TokenMetadata();
        tokenMetadata.setTokenId(token.getTokenId());
        tokenMetadata.setAddress(token.getNftContractAddress());
        try {
            System.out.println("Received Message From RabbitMQ: " + token);
            String imageCDNPath = "";
            JSONObject metadata = null;

            if (token.getTokenURI().startsWith("ipfs://") || token.getTokenURI().startsWith("https://")) {
                metadata = imageService.getMetadataFromTokenURI(formatIpfsURL(token.getTokenURI()));
                System.out.println("fetching image..");
                String imageIPFSURL = metadata.getString("image");

                if (imageIPFSURL != null && !imageIPFSURL.isEmpty()) {
                    if(imageIPFSURL.startsWith("ipfs://") || imageIPFSURL.startsWith("https://")){
                        imageIPFSURL = formatIpfsURL(imageIPFSURL);

                        imageCDNPath = downloadImageAndUploadToCDN(formatIpfsURL(imageIPFSURL));
                        System.out.println("image path in CDN " + imageCDNPath);
                    }else{
                        imageCDNPath = imageIPFSURL;
                    }
                }
            }else if(token.getTokenURI().startsWith("data:application/json;base64,")){
                String base64EncodedTokenURI = token.getTokenURI().replaceAll("data:application/json;base64,","");
                System.out.println("Base 64 : tokenURI : " + base64EncodedTokenURI);
                byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedTokenURI);
                String decodedTokenURIString = new String(decodedBytes);
                metadata = new JSONObject(decodedTokenURIString);
                String image = metadata.getString("image");

               if( image != null){
                   if((image.startsWith("ipfs://") || image.startsWith("https://"))){
                       imageCDNPath = downloadImageAndUploadToCDN(formatIpfsURL(image));
                   }else if(image.startsWith("data:application/json;base64,")){
                       imageCDNPath = image;
                   }
               }
            }

            tokenMetadata.setAssetCDNUrl(imageCDNPath);
            if(metadata != null)
                tokenMetadata.setMetadata(Document.parse(metadata.toString()));

            // saving metadata
            tokenMetadataRepository.saveMetadata(tokenMetadata);
        } catch (Exception e) {
            System.out.println("Exception inside received message : " + e.getMessage() + " ,cause : " + e.getCause());
        }
    }
}