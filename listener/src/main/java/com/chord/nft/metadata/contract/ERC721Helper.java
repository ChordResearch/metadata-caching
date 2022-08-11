package com.chord.nft.metadata.contract;

import com.chord.nft.metadata.contract.wrapper.Erc721;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class ERC721Helper {
    public static  String getTokenURI(Web3j web3j, String nftContractAddress, String tokenId) throws  Exception{
        Erc721 erc721 = Erc721.load(nftContractAddress,web3j,new ReadonlyTransactionManager(web3j,nftContractAddress),new DefaultGasProvider());
        return erc721.tokenURI(new BigInteger(tokenId)).send().toString();
    }
}
