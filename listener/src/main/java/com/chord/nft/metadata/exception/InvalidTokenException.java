package com.chord.nft.metadata.exception;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String errorMessage){
        super(errorMessage);
    }
}
