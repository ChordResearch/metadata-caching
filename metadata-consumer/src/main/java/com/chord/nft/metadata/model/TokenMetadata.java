package com.chord.nft.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenMetadata implements Serializable {
    private Document metadata;
    private String tokenId;
    private String address;
    private String assetCDNUrl;

    @Override
    public String toString() {
        return "TokenMetadata{" +
                "metadata=" + metadata +
                ", tokenId='" + tokenId + '\'' +
                ", address='" + address + '\'' +
                ", assetCDNUrl='" + assetCDNUrl + '\'' +
                '}';
    }
}
