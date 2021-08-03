package lcs.wang.bitcoin.cash.bech;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map.Entry;

import org.junit.Test;

public class BechCashUtilTest {

    BechCashUtil CASH = BechCashUtil.getInstance();

    @Test
    public void testEncodePayload() {
        String code = CASH.bechEncode(new byte[]{}, "prefix");
        assertEquals("prefix:x64nx6hz", code);
        code = CASH.bechEncode(new byte[]{}, "p");
        assertEquals("p:gpf8m4h7", code);
        byte[] res = CASH.bechDecode("qpzry9x8gf2tvdw0s3jn54khce6mua7l");
        code = CASH.bechEncode(res, "bitcoincash");
        assertEquals("bitcoincash:qpzry9x8gf2tvdw0s3jn54khce6mua7lcw20ayyn", code);
        res = CASH.bechDecode("555555555555555555555555555555555555555555555");
        code = CASH.bechEncode(res, "bchreg");
        assertEquals("bchreg:555555555555555555555555555555555555555555555udxmlmrz", code);
    }

    @Test
    public void testCashAddress2payload() {
        String org = "bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a";
        Entry<String, byte[]> entry = CASH.bechDecode(org, "");
        System.out.println(entry.getKey());
        // System.out.println(entry.getValue());
        String encode = CASH.bechEncode(entry.getValue(), "bitcoincash");
        assertEquals(org, encode);
    }

    @Test
    public void testLegacy2payload() {
        {
            // 1BpEi6DfDAUFd7GtittLSdBeYJvcoaVggu bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a
            String addr = CASH.encodeCashAdrressByLegacy("1BpEi6DfDAUFd7GtittLSdBeYJvcoaVggu");
            assertEquals("bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a", addr);
        }
        {
            String addr = CASH.encodeCashAdrressByLegacy("1KXrWXciRDZUpQwQmuM1DbwsKDLYAYsVLR");
            assertEquals("bitcoincash:qr95sy3j9xwd2ap32xkykttr4cvcu7as4y0qverfuy", addr);
        }

        {
            // 16w1D5WRVKJuZUsSRzdLp9w3YGcgoxDXb bitcoincash:qqq3728yw0y47sqn6l2na30mcw6zm78dzqre909m2r
            String addr = CASH.encodeCashAdrressByLegacy("16w1D5WRVKJuZUsSRzdLp9w3YGcgoxDXb");
            assertEquals("bitcoincash:qqq3728yw0y47sqn6l2na30mcw6zm78dzqre909m2r", addr);
        }
        {
            // 3CWFddi6m4ndiGyKqzYvsFYagqDLPVMTzC bitcoincash:ppm2qsznhks23z7629mms6s4cwef74vcwvn0h829pq
            String addr = CASH.encodeCashAdrressByLegacy("3CWFddi6m4ndiGyKqzYvsFYagqDLPVMTzC");
            assertEquals("bitcoincash:ppm2qsznhks23z7629mms6s4cwef74vcwvn0h829pq", addr);
        }
        {
            // 3LDsS579y7sruadqu11beEJoTjdFiFCdX4 bitcoincash:pr95sy3j9xwd2ap32xkykttr4cvcu7as4yc93ky28e
            String addr = CASH.encodeCashAdrressByLegacy("3LDsS579y7sruadqu11beEJoTjdFiFCdX4");
            assertEquals("bitcoincash:pr95sy3j9xwd2ap32xkykttr4cvcu7as4yc93ky28e", addr);
        }
        {
            // 31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37
            String addr = CASH.encodeCashAdrressByLegacy("31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw");
            assertEquals("bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37", addr);
        }
        {
            // 31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37
            String addr = CASH.encodeCashAdrressByLegacy("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
            assertEquals("bchtest:qqjr7yu573z4faxw8ltgvjwpntwys08fysk07zmvce", addr);
        }
        {
            // 31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37
            String addr = CASH.encodeCashAdrressByLegacy("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vc");
            assertEquals("bchtest:pp8f7ww2g6y07ypp9r4yendrgyznysc9kqxh6acwu3", addr);
        }
    }

}
