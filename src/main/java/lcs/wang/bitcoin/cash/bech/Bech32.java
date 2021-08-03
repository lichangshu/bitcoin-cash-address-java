package lcs.wang.bitcoin.cash.bech;

public class Bech32 {
    public static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    /**
     * The cashaddr character set for decoding.
     */
    public static final byte[] CHARSET_REV = { // 127
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 16+
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 2
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 3
            15, -1, 10, 17, 21, 20, 26, 30, +7, +5, -1, -1, -1, -1, -1, -1, // 4
            -1, 29, -1, 24, 13, 25, +9, +8, 23, -1, 18, 22, 31, 27, 19, -1, // 5
            +1, +0, +3, 16, 11, 28, 12, 14, +6, +4, +2, -1, -1, -1, -1, -1, // 6
            -1, 29, -1, 24, 13, 25, +9, +8, 23, -1, 18, 22, 31, 27, 19, -1, // 7
            +1, +0, +3, 16, 11, 28, 12, 14, 6, 4, 2, -1, -1, -1, -1, -1 ////// 8
    };

    private Bech32() {
    }

    public static final Bech32 BECH = new Bech32();

    /**
     * not 8 -> 5
     *
     * @param str
     * @return
     */
    public byte[] bechDecode(String str) {
        byte[] values = new byte[str.length()];
        for (int i = 0; i < str.length(); ++i) {
            byte c = (byte) str.charAt(i);
            // We have an invalid char in there.
            if (c > 127 || CHARSET_REV[c] == -1) {
                throw new IllegalArgumentException("Char at 0-127 ! But " + (int) c);
            }

            values[i] = CHARSET_REV[c];
        }
        return values;
    }

    /**
     * not do 8 -> 5 format
     *
     * @param payload
     * @return
     */
    public String bechEncode(byte[]... payload) {
        StringBuffer ret = new StringBuffer();
        for (byte[] cs : payload) {
            for (byte c : cs) {
                ret.append(CHARSET.charAt(c));
            }
        }
        return ret.toString();
    }

    /**
     * 5 -> 8
     *
     * @param payload
     * @return
     */
    public byte[] payloadDecode(byte[] payload) {
        return tailJoin(payload, 5);
    }

    /**
     *  8 --> 5
     * @param data
     * @return
     */
    public byte[] payloadEncode(byte[] data) {
        return byteSplit(data, 5);
    }

    public static byte[] byteSplit(byte[] bts, int unit) {
        return byteSplit(bts, unit, true);
    }

    public static byte[] byteSplit(byte[] bts, int unit, boolean pad) {
        if (unit > 8) {
            throw new IllegalArgumentException("unit < 8");
        }
        byte[] target = new byte[(bts.length * 8 + unit - 1) / unit];
        for (int i = 0; i < bts.length * 8; i++) { // simple algorithm
            byte v = bts[i / 8];
            byte t = target[i / unit];
            int i2 = 7 - (i % 8);
            target[i / unit] = (byte) ((t << 1) | (v >> i2 & 1));
        }
        if (pad) {
            int len = target.length;
            int less = bts.length * 8 % unit;
            if (less != 0) {
                target[len - 1] = (byte) (target[len - 1] << (unit - less));
            }
        }
        return target;
    }

    public static byte[] tailJoin(byte[] bts, int tail) {
        byte[] target = new byte[(bts.length * 5 + 7) / 8];
        for (int i = 0, k = 0; i < bts.length * 8; i++, k++) { // simple algorithm
            if (i % 8 == 0) {
                i += 8 - tail;
            }
            byte v = bts[i / 8];
            byte t = target[k / 8];
            int i2 = 7 - (i % 8);
            target[k / 8] = (byte) ((t << 1) | (v >> i2 & 1));
        }
        return target;
    }
}
