package com.chord.nft.metadata.event.handler;

import com.chord.nft.metadata.dto.EventParseResult;
import org.json.JSONObject;
import org.web3j.protocol.Web3j;

import java.sql.Connection;

public interface EventHandler {
    EventParseResult handleEvent(JSONObject event, Web3j web3j, Connection connection);

    boolean isValidEventTopic(String address, String topic);
}
