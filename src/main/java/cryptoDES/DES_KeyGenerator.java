package cryptoDES;

public class DES_KeyGenerator {
    private static final int[] PC1 = {
            57,49,41,33,25,17,9, 1,58,50,42,34,26,18,
            10,2,59,51,43,35,27, 19,11,3,60,52,44,36,
            63,55,47,39,31,23,15, 7,62,54,46,38,30,22,
            14,6,61,53,45,37,29, 21,13,5,28,20,12,4
    };

    private static final int[] PC2 = {
            14,17,11,24,1,5, 3,28,15,6,21,10, 23,19,12,4,26,8,
            16,7,27,20,13,2, 41,52,31,37,47,55, 30,40,51,45,33,48,
            44,49,39,56,34,53, 46,42,50,36,29,32
    };

    private static final int[] SHIFTS = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    public static byte[] permute(byte[] input, int[] table, int outputBits) {
        int outputBytes = (outputBits + 7) / 8;
        byte[] output = new byte[outputBytes];
        for (int i = 0; i < outputBits; i++) {
            int bitPosFrom = table[i] - 1;
            int bitVal = (input[bitPosFrom / 8] >> (7 - (bitPosFrom % 8))) & 1;
            output[i / 8] |= (byte) (bitVal << (7 - (i % 8)));
        }
        return output;
    }

    public byte[][] generateSubkeys(byte[] key64) {
        byte[] key56 = permute(key64, PC1, 56);
        byte[] L = getHalf(key56, 0);
        byte[] R = getHalf(key56, 28);
        byte[][] subkeys = new byte[16][6];

        for (int i = 0; i < 16; i++) {
            L = leftRotate28(L, SHIFTS[i]);
            R = leftRotate28(R, SHIFTS[i]);
            subkeys[i] = permute(joinHalves(L, R), PC2, 48);
        }
        return subkeys;
    }

    private byte[] getHalf(byte[] input, int startBit) {
        byte[] res = new byte[4];
        for (int i = 0; i < 28; i++) {
            int val = (input[(startBit + i) / 8] >> (7 - ((startBit + i) % 8))) & 1;
            res[i / 8] |= (byte) (val << (7 - (i % 8)));
        }
        return res;
    }

    private byte[] joinHalves(byte[] L, byte[] R) {
        byte[] res = new byte[7];
        for (int i = 0; i < 56; i++) {
            int val = (i < 28) ?
                    (L[i / 8] >> (7 - (i % 8))) & 1 :
                    (R[(i - 28) / 8] >> (7 - ((i - 28) % 8))) & 1;
            res[i / 8] |= (byte) (val << (7 - (i % 8)));
        }
        return res;
    }

    private byte[] leftRotate28(byte[] half, int shifts) {
        byte[] res = new byte[4];
        for (int i = 0; i < 28; i++) {
            int val = (half[((i + shifts) % 28) / 8] >> (7 - (((i + shifts) % 28) % 8))) & 1;
            res[i / 8] |= (byte) (val << (7 - (i % 8)));
        }
        return res;
    }
}