package com.chord.nft.metadata.exception;

public class NotMintOrBurnEventException extends Exception{
    public NotMintOrBurnEventException(String errorMessage){
        super(errorMessage);
    }
}
