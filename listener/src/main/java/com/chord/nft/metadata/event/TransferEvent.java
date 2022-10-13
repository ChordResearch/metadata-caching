package com.chord.nft.metadata.event;

import com.chord.nft.metadata.constant.AppConstants;
import com.chord.nft.metadata.entity.NFT;
import com.chord.nft.metadata.event.param.TransferEventParam;
import com.chord.nft.metadata.model.Token;
import com.chord.nft.metadata.service.RepositoryService;
import lombok.Data;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.sql.Connection;

@Data
public class TransferEvent implements Event {
    private TransferEventParam param;

    @Override
    public void save(Web3j web3j, RepositoryService repositoryService, Connection connection) throws Exception {
        NFT nft = new NFT();
        nft.setTokenAddress(param.getTokenAddress());
        nft.setTokenId(param.getTokenId());

        if (param.getTo().equals(AppConstants.ZERO_ADDRESS)) {
            repositoryService.getNftRepository().delete(connection, nft);
        } else {
            int retry = 10;

            EthBlock block = null;
            while(retry>0 && block == null) {
                try {
                    block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(param.getBlockNumber())), false).send();
                }catch (Exception e){
                    // do nothing
                }
                retry--;
            }
            long timestamp = block.getBlock().getTimestamp().longValue() * 1000;
            java.sql.Timestamp blockTimestamp = new java.sql.Timestamp(timestamp);
            nft.setMinterAddress(param.getMintedBy());


            String tokenURI = param.getTokenURI();

            // replacing non-utf8 invalid chars
            if (tokenURI != null)
                tokenURI = tokenURI.replace("\u0000", "");

            nft.setTokenURI(tokenURI);
            nft.setBlockNumber(Long.parseLong(param.getBlockNumber()));
            nft.setTransactionHash(param.getTransactionHash());
            nft.setCreatedAt(blockTimestamp);
            nft.setUpdatedAt(blockTimestamp);

            // Save newly minted token details
            System.out.println("Saving NFT : " + nft);
            repositoryService.getNftRepository().save(connection, nft);
            System.out.println("NFT details saved");

            // Publish to MQ for processing of metadata by consumer
            Token token = new Token();
            token.setTokenId(param.getTokenId());
            token.setNftContractAddress(param.getTokenAddress());
            token.setTokenURI(param.getTokenURI());
            repositoryService.getMqSender().send(token);
            System.out.println("Sent message into message queue");
        }
    }
}
