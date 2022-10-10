package com.chord.nft.metadata.service;


import com.chord.nft.metadata.dto.EventLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlockchainService {

    @Value("${network.url}")
    private String nodeURL;

    private static Web3j web3j;

    public Web3j getWeb3j() {
        if (web3j == null) {
            web3j = Web3j.build(new HttpService(nodeURL));
        }

        return web3j;
    }

    public String getCurrentNetworkBlock() throws Exception {
        return this.getWeb3j()
                .ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock()
                .getNumber()
                .toString();
    }

    public Date getBlockTimestamp(String blockNumber) throws Exception {
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(blockNumber)), false).send();
        return new Date(block.getBlock().getTimestamp().longValue());
    }

    public List<EthBlock.TransactionResult> getTransactionsByBlockNum(String blockNum) throws Exception {
        return this.getWeb3j()
                .ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(blockNum)), true)
                .send()
                .getBlock()
                .getTransactions();
    }

    public TransactionReceipt getTransactionReceiptByTxHash(String txHash) throws Exception {
        Optional<TransactionReceipt> receiptOptional = this.getWeb3j()
                .ethGetTransactionReceipt(txHash)
                .send()
                .getTransactionReceipt();

        if (receiptOptional.isPresent()) {
            return receiptOptional.get();
        }

        return null;
    }

    public Transaction getTransactionByHash(String txHash) throws Exception {
        Optional<Transaction> txOptional = this.getWeb3j().ethGetTransactionByHash(txHash).send().getTransaction();

        if (txOptional.isPresent()) {
            return txOptional.get();
        }
        return null;
    }

    public String getBlockHashByBlockNumber(String blockNumber) throws Exception {
        return this.getWeb3j().ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(blockNumber)), false).send().getBlock().getHash();
    }


    public List<EventLog> getEventLogs(String fromBlock, String toBlock, List<String> addresses, String[] topics) throws Exception {
        EthFilter filter = new EthFilter(new org.web3j.protocol.core.DefaultBlockParameterNumber(new BigInteger(fromBlock)), new org.web3j.protocol.core.DefaultBlockParameterNumber(new BigInteger(toBlock)), addresses);
        filter.addOptionalTopics(topics);


        Response<List<EthLog.LogResult>> response = web3j.ethGetLogs(filter).send();
        List<EthLog.LogResult> result = response.getResult();

        List<EventLog> eventLogs = new ArrayList<>();
        for (EthLog.LogResult r : result) {
            org.web3j.protocol.core.methods.response.Log log = (org.web3j.protocol.core.methods.response.Log) r.get();
            EventLog eventLog = new EventLog();
            eventLog.setAddress(log.getAddress());
            eventLog.setBlockHash(log.getBlockHash());
            eventLog.setBlockNumber(log.getBlockNumber().toString());
            eventLog.setData(log.getData());
            eventLog.setTopics(log.getTopics());
            eventLog.setTransactionHash(log.getTransactionHash());
            eventLog.setLogIndex(log.getLogIndex().toString());
            eventLogs.add(eventLog);
        }
        return eventLogs;
    }
}

