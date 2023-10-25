import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HashFunction {
    public static int hash(int x, int y, int size) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.flip();
        
        int seed = 0; // Change the seed value as needed
        
        long hash = murmurHash3(buffer.array(), seed);
        
        return Math.abs((int) hash)%size;
    }

    public static int hash2(int x, int y, int size) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.flip();

        int seed = 5; // Change the seed value as needed

        long hash = murmurHash3(buffer.array(), seed);

        return Math.abs((int) hash)%size;
    }

    private static long murmurHash3(byte[] data, int seed) {
        final long c1 = 0x87c37b91114253d5L;
        final long c2 = 0x4cf5ad432745937fL;
        final int r1 = 31;
        final int r2 = 27;
        final int r3 = 33;
        final int m = 5;
        final int n = 0x52dce729;

        long hash = seed;

        int length = data.length;
        int roundedEnd = length & 0xFFFFFFF0; // Round down to the nearest multiple of 16

        for (int i = 0; i < roundedEnd; i += 16) {
            long k1 = getLongLittleEndian(data, i);
            long k2 = getLongLittleEndian(data, i + 8);

            k1 *= c1;
            k1 = Long.rotateLeft(k1, r1);
            k1 *= c2;
            hash ^= k1;
            hash = Long.rotateLeft(hash, r2);
            hash = hash * m + n;

            k2 *= c2;
            k2 = Long.rotateLeft(k2, r3);
            k2 *= c1;
            hash ^= k2;
            hash = Long.rotateLeft(hash, r2);
            hash = hash * m + n;
        }

        long k1 = 0;
        long k2 = 0;

        switch (length & 0xF) {
            case 15:
                k2 ^= (long) (data[roundedEnd + 14] & 0xFF) << 48;
            case 14:
                k2 ^= (long) (data[roundedEnd + 13] & 0xFF) << 40;
            case 13:
                k2 ^= (long) (data[roundedEnd + 12] & 0xFF) << 32;
            case 12:
                k2 ^= (long) (data[roundedEnd + 11] & 0xFF) << 24;
            case 11:
                k2 ^= (long) (data[roundedEnd + 10] & 0xFF) << 16;
            case 10:
                k2 ^= (long) (data[roundedEnd + 9] & 0xFF) << 8;
            case 9:
                k2 ^= (long) (data[roundedEnd + 8] & 0xFF);

            case 8:
                k1 ^= (long) (data[roundedEnd + 7] & 0xFF) << 56;
            case 7:
                k1 ^= (long) (data[roundedEnd + 6] & 0xFF) << 48;
            case 6:
                k1 ^= (long) (data[roundedEnd + 5] & 0xFF) << 40;
            case 5:
                k1 ^= (long) (data[roundedEnd + 4] & 0xFF) << 32;
            case 4:
                k1 ^= (long) (data[roundedEnd + 3] & 0xFF) << 24;
            case 3:
                k1 ^= (long) (data[roundedEnd + 2] & 0xFF) << 16;
            case 2:
                k1 ^= (long) (data[roundedEnd + 1] & 0xFF) << 8;
            case 1:
                k1 ^= (long) (data[roundedEnd] & 0xFF);
                k1 *= c1;
                k1 = Long.rotateLeft(k1, r1);
                k1 *= c2;
                hash ^= k1;
                hash = Long.rotateLeft(hash, r2);
                hash = hash * m + n;
        }

        hash ^= length;
        hash = fmix(hash);

        return hash;
    }

    private static long getLongLittleEndian(byte[] data, int offset) {
        return ((long) data[offset + 7] << 56)
                | ((long) (data[offset + 6] & 0xFF) << 48)
                | ((long) (data[offset + 5] & 0xFF) << 40)
                | ((long) (data[offset + 4] & 0xFF) << 32)
                | ((long) (data[offset + 3] & 0xFF) << 24)
                | ((long) (data[offset + 2] & 0xFF) << 16)
                | ((long) (data[offset + 1] & 0xFF) << 8)
                | (data[offset] & 0xFF);
    }

    private static long fmix(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;

        return h;
    }
}
