package com.chord.nft.metadata.entity;

import lombok.Data;

@Data
public class Global {
    public Global() {
    }

    private long id;
    private long block;
    private long verifiedBlock;
    private boolean status;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
}
