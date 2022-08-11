package com.chord.nft.metadata.event.factory;

import com.chord.nft.metadata.event.handler.EventHandler;
import com.chord.nft.metadata.event.handler.TransferEventHandler;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Data
public class EventHandlerFactory {
    private Connection connection;

    @Autowired
    TransferEventHandler transferEventHandler;
    public EventHandler getEventHandler(JSONObject event) {
        String address = event.getString("address").toLowerCase();
        String topic1 = event.getJSONArray("topics").get(0).toString();
        if (transferEventHandler.isValidEventTopic(address, topic1)) {
            return transferEventHandler;
        } else
            return null;
    }
}
