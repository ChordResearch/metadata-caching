package com.chord.nft.metadata.service;


import com.chord.nft.metadata.constant.Graphql;
import com.chord.nft.metadata.dto.EventFilter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlockchainService {

    @Value("${network.url}")
    private String nodeURL;

    private static Web3j web3j;

    @Autowired
    GraphqlHttpService graphqlHttpService;

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


    public JSONArray getEvents(EventFilter filter) throws Exception {
        JSONObject eventFilterJson = new JSONObject();
        if (filter.getAddress().length > 0) {
            eventFilterJson.put("address", filter.getAddress());
        }
        if (filter.getFromBlock() != null && filter.getToBlock() != null) {
            eventFilterJson.put("fromBlock", filter.getFromBlock());
            eventFilterJson.put("toBlock", filter.getToBlock());
        }
        if (filter.getTopics() != null && filter.getTopics().length > 0) {
            eventFilterJson.put("topics", filter.getTopics());
        }

        JSONObject variablesJson = new JSONObject();
        variablesJson.put("eventFilter", eventFilterJson);

        String responseData = graphqlHttpService.post(Graphql.GET_EVENTS_QUERY, variablesJson.toString());

        JSONObject jsonResultObj = new JSONObject(responseData);
        JSONObject getEventJsonObj = new JSONObject(new JSONObject(jsonResultObj.get("data").toString()).get("GetEvents").toString());

        // data not found or error returned from core-api server
        if (getEventJsonObj.has("error") || !getEventJsonObj.has("data")) {
            String error = getEventJsonObj.get("error").toString();
            System.out.println("Error query for events from core-api: " + error);
            throw new Exception("Error querying events");
        }

        return new JSONArray(getEventJsonObj.get("data").toString());
    }
}

