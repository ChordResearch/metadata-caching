package com.chord.nft.metadata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
    private String address;
    private String blockHash;
    private String blockNumber;
    private String data;
    private List<String> topics;
    private String transactionHash;
    private String logIndex;
}
