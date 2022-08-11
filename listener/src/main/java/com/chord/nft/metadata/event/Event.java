package com.chord.nft.metadata.event;

import com.chord.nft.metadata.service.RepositoryService;

public interface Event {
    void save(org.web3j.protocol.Web3j web3j, RepositoryService repositoryService, java.sql.Connection connection) throws Exception;
}
