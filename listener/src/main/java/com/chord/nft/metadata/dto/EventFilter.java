package com.chord.nft.metadata.dto;

import lombok.Data;

@Data
public class EventFilter {
    private String[] address;
    private String fromBlock;
    private String toBlock;
    private String[][] topics;

    public EventFilter(String[] address, String fromBlock, String toBlock, String[][] topics) {
        this.address = address;
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.topics = topics;
    }
}
