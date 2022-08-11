package com.chord.nft.metadata.util;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public class HashUtil {
    public static String getKeccakhash(final String originalString){
        try{
            Keccak.Digest256 digest256 = new Keccak.Digest256();
            byte[] hashbytes = digest256.digest(originalString.getBytes(StandardCharsets.UTF_8));
            return "0x" + new String(Hex.encode(hashbytes));
        }catch (Exception e){
            return "";
        }
    }
}
