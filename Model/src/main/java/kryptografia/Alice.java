package kryptografia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Alice {
    Random random;

    private BigInteger prime;
    private BigInteger alfa;
    private BigInteger bobsPublicKey;
    private BigInteger alicesPrivateKey;
    private BigInteger alicesPublicKey;
    private BigInteger maskingKey;

    public Alice(BigInteger prime, BigInteger alfa, BigInteger bobsPublicKey) {
        this.random = new Random();
        this.prime = prime;
        this.alfa = alfa;
        this.bobsPublicKey = bobsPublicKey;
        this.alicesPrivateKey = BigInteger.probablePrime(255, this.random);
        this.alicesPublicKey = this.alfa.modPow(alicesPrivateKey, this.prime);
        this.maskingKey = bobsPublicKey.modPow(alicesPrivateKey, prime);
    }


    // must be sure message is always less than prime!
    // how?
    // use block of 16 bytes for example...
    public byte[] encrypt(byte[] block) {
        var bigIntBlock = new BigInteger(1,block);

        var bigIntResult = bigIntBlock.multiply(maskingKey).mod(prime);
        return bigIntResult.toByteArray();
    }

    public List<byte[]> encode(byte[] message) {
        int wholeBlocks = message.length / 16;
        boolean isEven = message.length % 16 == 0;
        List<byte[]> encodedBlocks = new ArrayList<>();

        int remainingBytes = message.length % 16;

        if (!isEven) {
            // create first block with 0s appended
            // 5 za duzo znakow
            // 16 - 5 = 11 - tyle zer dodac na poczatek
            byte[] block1st = new byte[16];
            for (int i = 0; i < 16 - remainingBytes; i++) {
                block1st[i] = 0;
            }
            int j = 0;
            for (int i = 16 - remainingBytes; i < 16; i++) {
                block1st[i] = message[j];
                j++;
            }

            encodedBlocks.add(encrypt(block1st));
        }

        // 5..
        for (int i = remainingBytes; i < remainingBytes + wholeBlocks*16; i+= 16) {
            byte[] block = new byte[16];
            System.arraycopy(message,i,block,0,16);
            encodedBlocks.add(encrypt(block));
        }

        return encodedBlocks;
    }

    public Random getRandom() {
        return random;
    }

    public BigInteger getPrime() {
        return prime;
    }

    public BigInteger getAlfa() {
        return alfa;
    }

    public BigInteger getBobsPublicKey() {
        return bobsPublicKey;
    }

    public BigInteger getAlicesPrivateKey() {
        return alicesPrivateKey;
    }

    public BigInteger getAlicesPublicKey() {
        return alicesPublicKey;
    }

    public BigInteger getMaskingKey() {
        return maskingKey;
    }
}
