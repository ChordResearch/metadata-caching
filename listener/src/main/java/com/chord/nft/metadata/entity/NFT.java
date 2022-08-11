package com.chord.nft.metadata.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NFT {
    private long id;
    private String tokenAddress;
    private String minterAddress;
    private String tokenId;
    private String tokenURI;
    private String blockNumber;
    private String transactionHash;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}