package com.chord.nft.metadata.event.handler;

import com.chord.nft.metadata.constant.AppConstants;
import com.chord.nft.metadata.contract.ERC721Helper;
import com.chord.nft.metadata.dto.EventParseResult;
import com.chord.nft.metadata.event.TransferEvent;
import com.chord.nft.metadata.event.param.TransferEventParam;
import com.chord.nft.metadata.exception.InvalidTokenException;
import com.chord.nft.metadata.exception.NotMintEventException;
import com.chord.nft.metadata.service.BlockchainService;
import com.chord.nft.metadata.util.HashUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.sql.Connection;
import java.util.List;

@Service
public class TransferEventHandler implements EventHandler {
    @Autowired
    private BlockchainService blockchainService;
    private TransferEventParam getTransferEventParams(JSONObject rawEvent, Web3j web3j,Connection connection) throws Exception {
        JSONArray jsonTopics = rawEvent.getJSONArray("topics");
        String fromTopic = jsonTopics.get(1).toString();
        String toTopic = jsonTopics.get(2).toString();

        Address from = (Address) FunctionReturnDecoder.decodeIndexedValue(fromTopic, new TypeReference<Address>(true) {
        });
        String fromAddress = from.getValue().toString().toLowerCase();

        if(!fromAddress.equals(AppConstants.ZERO_ADDRESS)) {
            throw new NotMintEventException("Token is not minted");
        }

        String tokenIdStr = null; // erc721 stores tokenId
        try{
            String tokenIdTopic = jsonTopics.get(3).toString();
            Uint256 value = (Uint256) FunctionReturnDecoder.decodeIndexedValue(tokenIdTopic, new TypeReference<Uint256>(true) {
            });
            tokenIdStr  = value.getValue().toString();
        } catch(Exception e){
            throw new InvalidTokenException("Invalid token");
        }

        Number blockNumber = rawEvent.getNumber("blockNumber");
        String tokenAddress = rawEvent.getString("address").toLowerCase();
        String tokenURI = ERC721Helper.getTokenURI(web3j, tokenAddress, tokenIdStr);
        String transactionHash = rawEvent.getString("transactionHash").toLowerCase();
        Transaction transaction = blockchainService.getTransactionByHash(transactionHash);
        String transactionFrom = transaction.getFrom().toLowerCase();

        Address to = (Address) FunctionReturnDecoder.decodeIndexedValue(toTopic, new TypeReference<Address>(true) {
        });

        TransferEventParam transferEventParam = new TransferEventParam(
                blockNumber.toString(),
                fromAddress,
                to.getValue().toString().toLowerCase(),
                tokenIdStr,
                tokenAddress,
                transactionFrom,
                transactionHash,
                tokenURI);

        return transferEventParam;
    }

    @Override
    public EventParseResult handleEvent(JSONObject event, Web3j web3j, Connection connection) {
        EventParseResult result = new EventParseResult();
        try {
            TransferEventParam eventParam = getTransferEventParams(event, web3j,connection);
            TransferEvent transferEvent = new TransferEvent();
            transferEvent.setParam(eventParam);

            result = new EventParseResult(true, transferEvent);
        } catch (Exception e) {
            // System.out.println("Exception in TransferEventHandler for address : "+ event.getString("address") + ", handleEvent() : " + e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public boolean isValidEventTopic(String address, String topic) {
        String topicSignature = "Transfer(address,address,uint256)";
        return HashUtil.getKeccakhash(topicSignature).equals(topic);
    }
}
