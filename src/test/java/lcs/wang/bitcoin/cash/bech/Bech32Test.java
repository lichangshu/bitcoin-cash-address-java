package lcs.wang.bitcoin.cash.bech;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Bech32Test {

    @Test
    public void testPayloadDecode() {
        byte[] data = new byte[]{0, 4, 127, -120, 56, 88, 1, -8, -9};
        byte[] target = Bech32.BECH.payloadEncode(data);
        byte[] res = Bech32.BECH.payloadDecode(target);
        assertArrayEquals(data, Arrays.copyOfRange(res, 0, data.length));
    }

    public void testPayloadEncode() {
    }
}