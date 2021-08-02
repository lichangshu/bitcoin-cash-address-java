package lcs.wang.bitcoin.cash.bech;

import java.util.Arrays;

public class BitArray {

    private final byte[] data;

    public BitArray(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public BitArray(byte[] data, int off, int len) {
        this.data = Arrays.copyOfRange(data, off, off + len);
    }

    /**
     * New byte[]
     * 
     * @return
     */
    public byte[] toArray() {
        return Arrays.copyOf(this.data, data.length);
    }

    public int bitLength() {
        return data.length * 8;
    }

    public int byteLength() {
        return data.length;
    }

    public byte getByte(int position) {
        return this.data[position];
    }

    /**
     * 
     * @param position
     * @param bt
     * @return this
     */
    public BitArray setByte(int position, byte bt) {
        this.data[position] = bt;
        return this;
    }

    /**
     * get 0 / 1
     * 
     * @param position
     * @return 0/1
     */
    public boolean get(int position) {
        if (position >= data.length * 8 || position < 0) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        int idx = position >> 3;// position / 8
        position = idx << 3 ^ position;// position % 8
        int dt = data[idx] << position & 0xFF;
        dt = dt >>> (8 - 1);
        return dt != 0;
    }

    /**
     * 
     * set 1
     * 
     * @param position
     * @return
     */
    public BitArray set(int position) {
        if (position >= data.length * 8 || position < 0) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        int idx = position >> 3;// position / 8
        position = idx << 3 ^ position;// position % 8
        data[idx] |= 1 << (8 - 1 - position);
        return this;
    }

    /**
     * set 0
     * 
     * @param position
     * @return
     */
    public BitArray clean(int position) {
        if (position >= data.length * 8 || position < 0) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        int idx = position >> 3;// position / 8
        position = idx << 3 ^ position;// position % 8
        data[idx] &= (1 << (8 - 1 - position) ^ 0xFF);
        return this;
    }

    @Override
    public String toString() {
        return this.toString(" ");
    }

    public String toString(String separator) {
        StringBuffer buf = new StringBuffer();
        for (byte b : this.data) {
            String bn = "00000000" + Integer.toBinaryString(b);
            buf.append(bn.substring(bn.length() - 8)).append(separator);
        }
        return buf.toString();
    }
}
