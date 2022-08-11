package com.chord.nft.metadata.repository;

import com.chord.nft.metadata.entity.NFT;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class NFTRepository extends Repository {
    public int save(Connection connection, NFT nft) {
        return getJdbcTemplate(connection).update("insert into nfts(" +
                        "\"tokenAddress\"," +
                        "\"minterAddress\"," +
                        "\"tokenId\"," +
                        "\"tokenURI\"," +
                        "\"blockNumber\"," +
                        "\"transactionHash\"," +
                        "\"createdAt\"," +
                        "\"updatedAt\"" +
                        ") values(?,?,?,?,?,?,?,?)",
                new Object[]{
                        nft.getTokenAddress(),
                        nft.getMinterAddress(),
                        nft.getTokenId(),
                        nft.getTokenURI(),
                        nft.getBlockNumber(),
                        nft.getTransactionHash(),
                        nft.getCreatedAt(),
                        nft.getUpdatedAt()
                });
    }
}
