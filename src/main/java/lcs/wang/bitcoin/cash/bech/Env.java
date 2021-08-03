package lcs.wang.bitcoin.cash.bech;

public enum Env {
    MAIN() {
        public byte getAddressHeader() {
            return 0;
        }

        public byte getDumpedPrivateKeyHeader() {
            return (byte) 128;
        }

        @Override
        public String cashPrefix() {
            return "bitcoincash";
        }

        @Override
        public String bitcoinPrefix() {
            return "bc";
        }
    },
    TEST() {
        public byte getAddressHeader() {
            return 111;
        }

        public byte getDumpedPrivateKeyHeader() {
            return (byte) 239;
        }

        public String cashPrefix() {
            return "bchtest";
        }

        @Override
        public String bitcoinPrefix() {
            return "tb";
        }
    }, REG_TEST() {
        public byte getAddressHeader() {
            return 111;
        }

        public byte getDumpedPrivateKeyHeader() {
            return (byte) 239;
        }

        public String cashPrefix() {
            return "bchreg";
        }

        @Override
        public String bitcoinPrefix() {
            return "bcrt";
        }
    };

    public abstract byte getAddressHeader();

    public abstract byte getDumpedPrivateKeyHeader();

    public abstract String cashPrefix();

    public abstract String bitcoinPrefix();

    public byte cashAddressHeader() {
        return 0;
    }
}
