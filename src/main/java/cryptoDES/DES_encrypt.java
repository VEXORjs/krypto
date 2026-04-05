package cryptoDES;

public class DES_encrypt {
    private final byte[] data;
    private final byte[] key;

    public DES_encrypt(String input, String keyStr) {
        this.data = pad(input.getBytes());
        byte[] k = new byte[8];
        System.arraycopy(keyStr.getBytes(), 0, k, 0, Math.min(keyStr.length(), 8));
        this.key = k;
    }

    private byte[] pad(byte[] in) {
        return getBytes(in);
    }

    static byte[] getBytes(byte[] in) {
        int p = 8 - (in.length % 8);
        byte[] out = new byte[in.length + p];
        System.arraycopy(in, 0, out, 0, in.length);
        for (int i = 0; i < p; i++) out[in.length + i] = (byte) p;
        return out;
    }

    public byte[] getData() { return data; }
    public byte[] getKey() { return key; }
}