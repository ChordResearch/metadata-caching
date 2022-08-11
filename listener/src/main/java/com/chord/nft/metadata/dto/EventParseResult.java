package com.chord.nft.metadata.dto;

import com.chord.nft.metadata.event.Event;
import lombok.Data;

@Data
public class EventParseResult {
    private boolean isValidEvent;
    private Event event;

    public EventParseResult() {
        super();
    }

    public EventParseResult(boolean isValidEvent, Event event) {
        this.isValidEvent = isValidEvent;
        this.event = event;
    }
}
