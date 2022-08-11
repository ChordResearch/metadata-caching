package com.chord.nft.metadata.event.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferEventParam {
    private String blockNumber;
    private String from;
    private String to;
    private String tokenId;
    private String tokenAddress;
    private String mintedBy;
    private String transactionHash;
    private String tokenURI;
}
