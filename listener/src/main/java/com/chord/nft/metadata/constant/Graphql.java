package com.chord.nft.metadata.constant;

public class Graphql {
    public static final String GET_EVENTS_QUERY = "query GetEvents($eventFilter: EventFilter) {\n" +
            "  GetEvents(eventFilter: $eventFilter) {\n" +
            "    ... on EventLogs {\n" +
            "      data {\n" +
            "        address\n" +
            "        data\n" +
            "        topics\n" +
            "        logIndex\n" +
            "        transactionIndex\n" +
            "        transactionHash\n" +
            "        blockHash\n" +
            "        blockNumber\n" +
            "      }\n" +
            "    }\n" +
            "    ... on InvalidInputParamError {\n" +
            "      error\n" +
            "    }\n" +
            "    ... on InternalError {\n" +
            "      error\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
