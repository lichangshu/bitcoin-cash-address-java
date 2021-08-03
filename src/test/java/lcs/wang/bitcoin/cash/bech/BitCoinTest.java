package lcs.wang.bitcoin.cash.bech;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class BitCoinTest {

    @Test
    public void sha256ripemd160() {// demo for https://en.bitcoin.it/wiki/Bech32
        byte[] bytes = new BigInteger("751e76e8199196d454941c45d1b3a323f1433bd6", 16).toByteArray();
        byte[] bytes1 = Bech32.BECH.payloadEncode(bytes);
        byte[] hex = new BigInteger("0e140f070d1a001912060b0d081504140311021d030c1d03040f1814060e1e16", 16).toByteArray();
        assertArrayEquals(bytes1, hex);
        byte[] nhex = new byte[hex.length + 1];
        System.arraycopy(hex, 0, nhex, 1, hex.length);
        byte[] bcs = BitCoin.BECH.createChecksum("bc", nhex, BitCoin.Encode.BECH32);
        assertArrayEquals(new BigInteger("0c0709110b15", 16).toByteArray(), bcs);
        String s = Bech32.BECH.bechEncode(nhex, bcs);
        assertEquals("qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4", s);
    }
}