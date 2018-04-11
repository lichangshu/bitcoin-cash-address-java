package lcs.wang.bitcoin.cash.bech;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.junit.Test;

import lcs.wang.bitcoin.cash.bech.BechCashUtil;

public class BechCashUtilTest {

    BechCashUtil CASH = BechCashUtil.getInstance();

    @Test
    public void testEncode() {
        String code = CASH.encodePayload("prefix", new byte[] {});
        assertEquals("prefix:x64nx6hz", code);
        code = CASH.encodePayload("p", new byte[] {});
        assertEquals("p:gpf8m4h7", code);
        byte[] res = CASH.cashAddress2payload("qpzry9x8gf2tvdw0s3jn54khce6mua7l");
        code = CASH.encodePayload("bitcoincash", res);
        assertEquals("bitcoincash:qpzry9x8gf2tvdw0s3jn54khce6mua7lcw20ayyn", code);
        res = CASH.cashAddress2payload("555555555555555555555555555555555555555555555");
        code = CASH.encodePayload("bchreg", res);
        assertEquals("bchreg:555555555555555555555555555555555555555555555udxmlmrz", code);
    }

    @Test
    public void testDecode() {
        String org = "bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a";
        Entry<String, byte[]> entry = CASH.cashAddress2payload(org, "");
        System.out.println(entry.getKey());
        // System.out.println(entry.getValue());
        String encode = CASH.encodePayload("bitcoincash", entry.getValue());
        assertEquals(org, encode);
    }

    @Test
    public void testtransitionToBech() {
        {
            // 1BpEi6DfDAUFd7GtittLSdBeYJvcoaVggu bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a
            byte[] code = CASH.legacy2payload("1BpEi6DfDAUFd7GtittLSdBeYJvcoaVggu");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a", addr);
        }
        {
            byte[] code = CASH.legacy2payload("1KXrWXciRDZUpQwQmuM1DbwsKDLYAYsVLR");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:qr95sy3j9xwd2ap32xkykttr4cvcu7as4y0qverfuy", addr);
        }

        {
            // 16w1D5WRVKJuZUsSRzdLp9w3YGcgoxDXb bitcoincash:qqq3728yw0y47sqn6l2na30mcw6zm78dzqre909m2r
            byte[] code = CASH.legacy2payload("16w1D5WRVKJuZUsSRzdLp9w3YGcgoxDXb");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:qqq3728yw0y47sqn6l2na30mcw6zm78dzqre909m2r", addr);
        }
        {
            // 3CWFddi6m4ndiGyKqzYvsFYagqDLPVMTzC bitcoincash:ppm2qsznhks23z7629mms6s4cwef74vcwvn0h829pq
            byte[] code = CASH.legacy2payload("3CWFddi6m4ndiGyKqzYvsFYagqDLPVMTzC");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:ppm2qsznhks23z7629mms6s4cwef74vcwvn0h829pq", addr);
        }
        {
            // 3LDsS579y7sruadqu11beEJoTjdFiFCdX4 bitcoincash:pr95sy3j9xwd2ap32xkykttr4cvcu7as4yc93ky28e
            byte[] code = CASH.legacy2payload("3LDsS579y7sruadqu11beEJoTjdFiFCdX4");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:pr95sy3j9xwd2ap32xkykttr4cvcu7as4yc93ky28e", addr);
        }
        {
            // 31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37
            byte[] code = CASH.legacy2payload("31nwvkZwyPdgzjBJZXfDmSWsC4ZLKpYyUw");
            String addr = CASH.encodePayload("bitcoincash", code);
            assertEquals("bitcoincash:pqq3728yw0y47sqn6l2na30mcw6zm78dzq5ucqzc37", addr);
        }
    }

}
