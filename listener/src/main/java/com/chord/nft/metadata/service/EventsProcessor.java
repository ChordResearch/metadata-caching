package com.chord.nft.metadata.service;

import com.chord.nft.metadata.dto.EventParseResult;
import com.chord.nft.metadata.event.factory.EventHandlerFactory;
import com.chord.nft.metadata.event.handler.EventHandler;
import org.json.JSONObject;
import org.web3j.protocol.Web3j;

import java.sql.Connection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class EventsProcessor implements Callable<EventParseResult> {
    private JSONObject event;
    private CountDownLatch latch;

    private Web3j web3j;
    private EventHandlerFactory eventsHandlerFactory;

    private Connection connection;

    public EventsProcessor(JSONObject event, CountDownLatch latch, EventHandlerFactory eventsHandlerFactory, Web3j web3j, Connection connection) {
        this.latch = latch;
        this.event = event;
        this.eventsHandlerFactory = eventsHandlerFactory;
        this.web3j = web3j;
        this.connection = connection;
    }

    @Override
    public EventParseResult call() {
        EventParseResult eventParseResult = new EventParseResult();
        try {
            EventHandler eventHandler = eventsHandlerFactory.getEventHandler(event);
            eventParseResult = eventHandler.handleEvent(event, web3j, connection);
        } catch (Exception e) {
            // System.out.println("Exception inside call() for event " + event.toString());
        }

        latch.countDown();

        return eventParseResult;
    }
}


