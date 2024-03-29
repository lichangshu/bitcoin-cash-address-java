package lcs.wang.bitcoin.cash.bech;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map.Entry;

// C++ conversion to java
// DOC : https://github.com/bitcoincashorg/spec/blob/master/cashaddr.md
// CODE : https://github.com/Bitcoin-ABC/bitcoin-abc/blob/6c9c42ccb093820d5dd6f32f02c657c25ce5f823/src/cashaddr.cpp
public class BitCash {

    private BitCash() {
    }

    public static final BitCash CASH = new BitCash();

    /**
     * This function will compute what 8 5-bit values to XOR into the last 8 input values, in order to make the checksum 0. These 8 values are packed together in a single 40-bit
     * integer. The higher bits correspond to earlier values.
     */
    public long polyMod(byte[] v) {
        /**
         * The input is interpreted as a list of coefficients of a polynomial over F = GF(32), with an implicit 1 in front. If the input is [v0,v1,v2,v3,v4], that polynomial is v(x) =
         * 1*x^5 + v0*x^4 + v1*x^3 + v2*x^2 + v3*x + v4. The implicit 1 guarantees that [v0,v1,v2,...] has a distinct checksum from [0,v0,v1,v2,...].
         *
         * The output is a 40-bit integer whose 5-bit groups are the coefficients of the remainder of v(x) mod g(x), where g(x) is the cashaddr generator, x^8 + {19}*x^7 + {3}*x^6 +
         * {25}*x^5 + {11}*x^4 + {25}*x^3 + {3}*x^2 + {19}*x + {1}. g(x) is chosen in such a way that the resulting code is a BCH code, guaranteeing detection of up to 4 errors within a
         * window of 1025 characters. Among the various possible BCH codes, one was selected to in fact guarantee detection of up to 5 errors within a window of 160 characters and 6 erros
         * within a window of 126 characters. In addition, the code guarantee the detection of a burst of up to 8 errors.
         *
         * Note that the coefficients are elements of GF(32), here represented as decimal numbers between {}. In this finite field, addition is just XOR of the corresponding numbers. For
         * example, {27} + {13} = {27 ^ 13} = {22}. Multiplication is more complicated, and requires treating the bits of values themselves as coefficients of a polynomial over a smaller
         * field, GF(2), and multiplying those polynomials mod a^5 + a^3 + 1. For example, {5} * {26} = (a^2 + 1) * (a^4 + a^3 + a) = (a^4 + a^3 + a) * a^2 + (a^4 + a^3 + a) = a^6 + a^5 +
         * a^4 + a = a^3 + 1 (mod a^5 + a^3 + 1) = {9}.
         *
         * During the course of the loop below, `c` contains the bitpacked coefficients of the polynomial constructed from just the values of v that were processed so far, mod g(x). In the
         * above example, `c` initially corresponds to 1 mod (x), and after processing 2 inputs of v, it corresponds to x^2 + v0*x + v1 mod g(x). As 1 mod g(x) = 1, that is the starting
         * value for `c`.
         */
        long c = 1;
        for (byte d : v) {
            /**
             * We want to update `c` to correspond to a polynomial with one extra term. If the initial value of `c` consists of the coefficients of c(x) = f(x) mod g(x), we modify it to
             * correspond to c'(x) = (f(x) * x + d) mod g(x), where d is the next input to process.
             *
             * <pre>
             * Simplifying:
             * c'(x) = (f(x) * x + d) mod g(x)
             *         ((f(x) mod g(x)) * x + d) mod g(x)
             *         (c(x) * x + d) mod g(x)
             * If c(x) = c0*x^5 + c1*x^4 + c2*x^3 + c3*x^2 + c4*x + c5, we want to
             * compute
             * c'(x) = (c0*x^5 + c1*x^4 + c2*x^3 + c3*x^2 + c4*x + c5) * x + d
             *                                                             mod g(x)
             *       = c0*x^6 + c1*x^5 + c2*x^4 + c3*x^3 + c4*x^2 + c5*x + d
             *                                                             mod g(x)
             *       = c0*(x^6 mod g(x)) + c1*x^5 + c2*x^4 + c3*x^3 + c4*x^2 +
             *                                                             c5*x + d
             * If we call (x^6 mod g(x)) = k(x), this can be written as
             * c'(x) = (c1*x^5 + c2*x^4 + c3*x^3 + c4*x^2 + c5*x + d) + c0*k(x)
             * </pre>
             */

            // First, determine the value of c0:
            byte c0 = (byte) (c >>> 35);

            // Then compute c1*x^5 + c2*x^4 + c3*x^3 + c4*x^2 + c5*x + d:
            c = ((c & 0x07ffffffffL) << 5) ^ d;

            // Finally, for each set bit n in c0, conditionally add {2^n}k(x):
            if ((c0 & 0x01) != 0) {
                // k(x) = {19}*x^7 + {3}*x^6 + {25}*x^5 + {11}*x^4 + {25}*x^3 +
                // {3}*x^2 + {19}*x + {1}
                c ^= 0x98f2bc8e61L;
            }

            if ((c0 & 0x02) != 0) {
                // {2}k(x) = {15}*x^7 + {6}*x^6 + {27}*x^5 + {22}*x^4 + {27}*x^3 +
                // {6}*x^2 + {15}*x + {2}
                c ^= 0x79b76d99e2L;
            }

            if ((c0 & 0x04) != 0) {
                // {4}k(x) = {30}*x^7 + {12}*x^6 + {31}*x^5 + {5}*x^4 + {31}*x^3 +
                // {12}*x^2 + {30}*x + {4}
                c ^= 0xf33e5fb3c4L;
            }

            if ((c0 & 0x08) != 0) {
                // {8}k(x) = {21}*x^7 + {24}*x^6 + {23}*x^5 + {10}*x^4 + {23}*x^3 +
                // {24}*x^2 + {21}*x + {8}
                c ^= 0xae2eabe2a8L;
            }

            if ((c0 & 0x10) != 0) {
                // {16}k(x) = {3}*x^7 + {25}*x^6 + {7}*x^5 + {20}*x^4 + {7}*x^3 +
                // {25}*x^2 + {3}*x + {16}
                c ^= 0x1e4f43e470L;
            }
        }

        /**
         * PolyMod computes what value to xor into the final values to make the checksum 0. However, if we required that the checksum was 0, it would be the case that appending a 0 to a
         * valid list of values would result in a new valid list. For that reason, cashaddr requires the resulting checksum to be 1 instead.
         */
        return c ^ 1;
    }

    public char lowerCase(char c) {
        // ASCII black magic.
        return (char) (c | 0x20);
    }

    public byte[] expandPrefix(String prefix) {
        byte[] ret = new byte[prefix.length() + 1];
        for (int i = 0; i < prefix.length(); ++i) {
            ret[i] = (byte) (prefix.charAt(i) & 0x1f);
        }
        ret[prefix.length()] = 0;
        return ret;
    }

    public boolean verifyChecksum(String prefix, byte[] payload) {
        return polyMod(Bech32.concat(expandPrefix(prefix), payload)) == 0;
    }

    public byte[] createChecksum(String prefix, byte[] payload) {
        byte[] enc = Bech32.concat(expandPrefix(prefix), payload, new byte[8]);// Append 8 zeroes.
        // Determine what to XOR into those 8 zeroes.
        long mod = polyMod(enc);
        byte[] ret = new byte[8];
        for (int i = 0; i < 8; ++i) {
            // Convert the 5-bit groups in mod to checksum values.
            ret[i] = (byte) ((mod >> (5 * (7 - i))) & 0x1f);
        }

        return ret;
    }

    public String bechEncode(byte[] payload, Env prefix) {
        byte[] checksum = createChecksum(prefix.cashPrefix(), payload);
        return prefix.cashPrefix() + ":" + Bech32.BECH.bechEncode(payload, checksum);
    }

    /**
     * not do 8 -> 5 format
     *
     * @param prefix
     * @param payload
     * @return
     */
    public String bechEncode(byte[] payload, String prefix) {
        byte[] checksum = createChecksum(prefix, payload);
        return prefix + ":" + Bech32.BECH.bechEncode(payload, checksum);
    }

    /**
     * Decode a cashaddr string. 5 format !
     */
    public Entry<String, byte[]> bechDecode(String str, String defaultPrefix) {
        // Go over the string and do some sanity checks.
        boolean lower = false, upper = false, hasNumber = false;
        int prefixSize = 0;
        for (int i = 0; i < str.length(); ++i) {
            byte c = (byte) str.charAt(i);
            if (c >= 'a' && c <= 'z') {
                lower = true;
                continue;
            }

            if (c >= 'A' && c <= 'Z') {
                upper = true;
                continue;
            }

            if (c >= '0' && c <= '9') {
                // We cannot have numbers in the prefix.
                hasNumber = true;
                continue;
            }

            if (c == ':') {
                // The separator cannot be the first character, cannot have number
                // and there must not be 2 separators.
                if (hasNumber || i == 0 || prefixSize != 0) {
                    throw new IllegalArgumentException("The separator cannot be the first character, cannot have number and there must not be 2 separators");
                }

                prefixSize = i;
                continue;
            }

            // We have an unexpected character.
            throw new IllegalArgumentException("Have an unexpected character." + (char) c);
        }

        // We can't have both upper case and lowercase.
        if (upper && lower) {
            throw new IllegalArgumentException("can't have both uppercase and lowercase");
        }

        // Get the prefix.
        StringBuffer prefix = new StringBuffer();
        if (prefixSize == 0) {
            prefix.append(defaultPrefix);
        } else {
            for (int i = 0; i < prefixSize; ++i) {
                prefix.append(lowerCase(str.charAt(i)));
            }

            // Now add the ':' in the size.
            prefixSize++;
        }

        // Decode values.
        byte[] values = Bech32.BECH.bechDecode(str.substring(prefixSize));

        // Verify the checksum.
        if (!verifyChecksum(prefix.toString(), values)) {
            throw new IllegalArgumentException("VerifyChecksum error");
        }
        // 40 bit checksum
        return new SimpleEntry<>(prefix.toString(), Arrays.copyOf(values, values.length - 8));
    }

    // ---------------------------
    // 上面代码修改自 C++ 下面为添加
    // ---------------------------

    /**
     * bch old address to new address (payload)
     *
     * @param btc
     * @return
     */
    public String encodeCashAdrressByLegacy(String btc) {
        byte[] b58 = Base58.decode(btc);
        if (b58.length != 25) {
            throw new IllegalArgumentException("Bitcoin address is 25 bytes !");
        }
        byte ver = b58[0];
        // 1 byte version + 20 byte data(sha160) + 4 bytes check code
        int len = 1 + 20;// 21
        // P2KH 0->0, P2SH 5 -> 8
        // prefix doc list : https://en.bitcoin.it/wiki/List_of_address_prefixes
        String prefix;
        if (ver == 0) {
            b58[0] = 0;
            prefix = "bitcoincash";
        } else if (ver == 5) {
            b58[0] = 8;
            prefix = "bitcoincash";
        } else if (ver == 0x6F) {
            b58[0] = 0;
            prefix = "bchtest";
        } else if (ver == (byte) 0xC4) {
            b58[0] = 8;
            prefix = "bchtest";
        } else {
            throw new IllegalArgumentException("unsupport format");
        }
        byte[] payload = Bech32.BECH.payloadEncode(Arrays.copyOf(b58, len));
        if (ver == 0) {
            prefix = "bitcoincash";
        } else if (ver == 0x6F) {
            prefix = "bchtest";
        }
        String address = bechEncode(payload, prefix);
        return address;
    }

}
