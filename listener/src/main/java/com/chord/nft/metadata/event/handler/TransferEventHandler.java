package com.chord.nft.metadata.event.handler;

import com.chord.nft.metadata.constant.AppConstants;
import com.chord.nft.metadata.contract.ERC721Helper;
import com.chord.nft.metadata.dto.EventLog;
import com.chord.nft.metadata.dto.EventParseResult;
import com.chord.nft.metadata.event.TransferEvent;
import com.chord.nft.metadata.event.param.TransferEventParam;
import com.chord.nft.metadata.exception.InvalidTokenException;
import com.chord.nft.metadata.exception.NotMintEventException;
import com.chord.nft.metadata.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.sql.Connection;

@Service
public class TransferEventHandler implements EventHandler {
    private static final String TRANSFER_TOPIC = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    @Autowired
    private BlockchainService blockchainService;
    private TransferEventParam getTransferEventParams(EventLog eventLog, Web3j web3j, Connection connection) throws Exception {

        String fromTopic = eventLog.getTopics().get(1);
        String toTopic = eventLog.getTopics().get(2);

        Address from = (Address) FunctionReturnDecoder.decodeIndexedValue(fromTopic, new TypeReference<Address>(true) {
        });
        String fromAddress = from.getValue().toString().toLowerCase();

        if(!fromAddress.equals(AppConstants.ZERO_ADDRESS)) {
            throw new NotMintEventException("Token is not minted");
        }

        String tokenIdStr = null; // erc721 stores tokenId
        try{
            String tokenIdTopic = eventLog.getTopics().get(3);
            Uint256 value = (Uint256) FunctionReturnDecoder.decodeIndexedValue(tokenIdTopic, new TypeReference<Uint256>(true) {
            });
            tokenIdStr  = value.getValue().toString();
        } catch(Exception e){
            throw new InvalidTokenException("Invalid token");
        }

        String blockNumber = eventLog.getBlockNumber();
        String tokenAddress = eventLog.getAddress().toLowerCase();
        String tokenURI = ERC721Helper.getTokenURI(web3j, tokenAddress, tokenIdStr);
        String transactionHash = eventLog.getTransactionHash().toLowerCase();
        Transaction transaction = blockchainService.getTransactionByHash(transactionHash);
        String transactionFrom = transaction.getFrom().toLowerCase();

        Address to = (Address) FunctionReturnDecoder.decodeIndexedValue(toTopic, new TypeReference<Address>(true) {
        });

        TransferEventParam transferEventParam = new TransferEventParam(
                blockNumber,
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
    public EventParseResult handleEvent(EventLog event, Web3j web3j, Connection connection) {
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
        return TRANSFER_TOPIC.equals(topic);
    }
}
