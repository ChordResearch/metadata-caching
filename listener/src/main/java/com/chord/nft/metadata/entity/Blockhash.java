package com.chord.nft.metadata.entity;

import lombok.Data;

@Data
public class Blockhash {
    public Blockhash() {
    }

    private long id;
    private long block;
    private String blockhash;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
}
