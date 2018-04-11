package lcs.wang.bitcoin.cash.bech;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitArrayTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGet() {
        byte[] bt = new byte[] { 1, 2, 5, 6, 0, 9, 10, -1, 9 };
        int len = bt.length * 8;
        String oby = "";
        for (byte b : bt) {
            String bn = "00000000" + Integer.toBinaryString(b);
            bn = bn.substring(bn.length() - 8);
            System.out.print(String.format("%s ", bn));
            oby += bn;
        }
        System.out.println();
        {
            BitArray arr = new BitArray(bt);
            for (int i = 0; i < len; i++) {
                // assertEquals
                assertEquals(oby.charAt(i), arr.get(i) ? '1' : '0');
                // assertEquals
                System.out.print(arr.get(i) ? 1 : 0);
                if (i % 8 == 7) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testSet() {
        byte[] bt = new byte[] { 1, 2, 5, 6, 0, 9, 10, -1, 9 };
        int len = bt.length * 8;
        String oby = "";
        for (byte b : bt) {
            String bn = "00000000" + Integer.toBinaryString(b);
            bn = bn.substring(bn.length() - 8);
            System.out.print(String.format("%s ", bn));
            oby += bn;
        }
        System.out.println();
        {
            BitArray arr = new BitArray(bt);
            arr.set(0);
            arr.set(8);
            arr.set(21);
            arr.set(62);
            for (int i = 0; i < len; i++) {
                // assertEquals
                if (i == 0) {
                    assertTrue(arr.get(i));
                } else if (i == 8) {
                    assertTrue(arr.get(i));
                } else {
                    assertEquals(oby.charAt(i), arr.get(i) ? '1' : '0');
                }
                // assertEquals
                System.out.print(arr.get(i) ? 1 : 0);
                if (i % 8 == 7) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testClean() {
        byte[] bt = new byte[] { 1, 2, 5, 6, 0, 9, 10, -1, 9 };
        int len = bt.length * 8;
        String oby = "";
        for (byte b : bt) {
            String bn = "00000000" + Integer.toBinaryString(b);
            bn = bn.substring(bn.length() - 8);
            System.out.print(String.format("%s ", bn));
            oby += bn;
        }
        System.out.println();
        {
            BitArray arr = new BitArray(bt);
            arr.clean(0);
            arr.clean(7);
            arr.clean(71);
            for (int i = 0; i < len; i++) {
                // assertEquals
                if (i == 0) {
                    assertFalse(arr.get(i));
                } else if (i == 7) {
                    assertFalse(arr.get(i));
                } else if (i == 71) {
                    assertFalse(arr.get(i));
                } else {
                    assertEquals(oby.charAt(i), arr.get(i) ? '1' : '0');
                }
                // assertEquals
                System.out.print(arr.get(i) ? 1 : 0);
                if (i % 8 == 7) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
