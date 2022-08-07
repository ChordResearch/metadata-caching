package com.chord.nft.metadata.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Token.class)
@Data
public class Token {
    private String nftContractAddress;
    private String tokenId;

    @Override
    public String toString() {
        return "Token{" +
                "nftContractAddress='" + nftContractAddress + '\'' +
                ", tokenId='" + tokenId + '\'' +
                '}';
    }
}
