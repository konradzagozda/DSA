package kryptografia;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class Bob {
    Random random = new Random();

    private BigInteger p,q,g,y;

//    private BigInteger prime;
//    private BigInteger alfa;
//    private BigInteger bobsPublicKey;
//    private BigInteger alicesPrivateKey;
//    private BigInteger alicesPublicKey;
//    private BigInteger maskingKey;

    public Bob(BigInteger p, BigInteger q, BigInteger g, BigInteger y) {
        this.p = p;
        this.q = q;
        this.g = g;
        this.y = y;


//        this.prime = prime;
//        this.alfa = alfa;
//        this.bobsPublicKey = bobsPublicKey;
//        this.alicesPrivateKey = BigInteger.probablePrime(255, this.random);
//        this.alicesPublicKey = this.alfa.modPow(alicesPrivateKey, this.prime);
//        this.maskingKey = bobsPublicKey.modPow(alicesPrivateKey, prime);
    }

    public BigInteger HashBytes(byte[] message) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] encodedhash = digest.digest(message);
        return new BigInteger(encodedhash);
    }

    public boolean verifySignature(BigInteger r, BigInteger s, byte[] message) {
        BigInteger w = s.modInverse(q);

        BigInteger hash = HashBytes(message);
        BigInteger u1 = hash.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = (g.modPow(u1,p).multiply(y.modPow(u2,p))).mod(p).mod(q);
//        System.out.println(v);
//        System.out.println(r);
        return v.equals(r);
    }



//    // must be sure message is always less than prime!
//    // how?
//    // use block of 16 bytes for example...
//    public byte[] encrypt(byte[] block) {
//        var bigIntBlock = new BigInteger(1,block);
//
//        var bigIntResult = bigIntBlock.multiply(maskingKey).mod(prime);
//        return bigIntResult.toByteArray();
//    }
//
//    public List<byte[]> encode(byte[] message) {
//        int wholeBlocks = message.length / 16;
//        boolean isEven = message.length % 16 == 0;
//        List<byte[]> encodedBlocks = new ArrayList<>();
//
//        int remainingBytes = message.length % 16;
//
//        if (!isEven) {
//            // create first block with 0s appended
//            // 5 za duzo znakow
//            // 16 - 5 = 11 - tyle zer dodac na poczatek
//            byte[] block1st = new byte[16];
//            for (int i = 0; i < 16 - remainingBytes; i++) {
//                block1st[i] = 0;
//            }
//            int j = 0;
//            for (int i = 16 - remainingBytes; i < 16; i++) {
//                block1st[i] = message[j];
//                j++;
//            }
//
//            encodedBlocks.add(encrypt(block1st));
//        }
//
//        // 5..
//        for (int i = remainingBytes; i < remainingBytes + wholeBlocks*16; i+= 16) {
//            byte[] block = new byte[16];
//            System.arraycopy(message,i,block,0,16);
//            encodedBlocks.add(encrypt(block));
//        }
//
//        return encodedBlocks;
//    }
//
//    public Random getRandom() {
//        return random;
//    }
//
//    public BigInteger getPrime() {
//        return prime;
//    }
//
//    public BigInteger getAlfa() {
//        return alfa;
//    }
//
//    public BigInteger getBobsPublicKey() {
//        return bobsPublicKey;
//    }
//
//    public BigInteger getAlicesPrivateKey() {
//        return alicesPrivateKey;
//    }
//
//    public BigInteger getAlicesPublicKey() {
//        return alicesPublicKey;
//    }
//
//    public BigInteger getMaskingKey() {
//        return maskingKey;
//    }
}
