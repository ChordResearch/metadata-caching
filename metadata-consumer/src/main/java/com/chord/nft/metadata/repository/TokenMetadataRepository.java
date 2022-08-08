package com.chord.nft.metadata.repository;

import com.chord.nft.metadata.model.TokenMetadata;

public interface TokenMetadataRepository {
    public TokenMetadata saveMetadata(TokenMetadata metadata);

    public TokenMetadata findTokenMetadata(String address, String tokenId);
}
