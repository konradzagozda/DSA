package kryptografia;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * p, q, g - publiczne parametry
 * x - klucz prywatny
 * y - klucz publiczny
 */
public class Alice {
    private Random random;

    private BigInteger p, q, g, x, y, r, s;

    public Alice() {
        random = new Random();

        // generate params + keys
        var pq = generatePQ();
        this.p = pq[0];
        this.q = pq[1];
        this.g = calculateG();
        this.x = chooseX();
        this.y = calculateY();

        System.out.println("P: " + p);
        System.out.println("Q: " + q);
        System.out.println("G: " + g);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
    }

    /**
     * @param message wiadomosc do podpisania
     * @return {r,s} - podpis
     */
    public BigInteger[] sign(byte[] message) {
        // generate k < q
        BigInteger k = new BigInteger(160, random).mod(q);

        // calculate r,s
        r = g.modPow(k,p).mod(q);

        BigInteger hash = HashBytes(message);
        s = k.modInverse(q).multiply(hash.add(x.multiply(r))).mod(q);
        return new BigInteger[]{r,s};
    }


//    public byte[] decrypt(byte[] block) {
//        var bigIntBlock = new BigInteger(1,block);
//
//        var bigIntResult = bigIntBlock.multiply(invMaskingKey).mod(prime);
//        var result =  bigIntResult.toByteArray();
//
//        if (result.length > 16){
//            byte[] tmp = new byte[16];
//            System.arraycopy(result,result.length-16,tmp,0,16);
//            result = tmp;
//        }
//
//        if (result.length < 16) {
//            byte[] tmp = new byte[16];
//            System.arraycopy(result,0, tmp,16-result.length, result.length);
//            result = tmp;
//        }
//
//        return result;
//    }

//    public byte[] decode(List<byte[]> encodedBlocks) {
//        byte[] decodedMessage = new byte[encodedBlocks.size() * 16];
//
//        for (int i = 0; i < encodedBlocks.size(); i++) {
//            byte[] decryptedBlock = decrypt(encodedBlocks.get(i));
//            System.arraycopy(decryptedBlock,0,decodedMessage,16*i,16);
//        }
//
//
//        // remove leading zeros
//        int i = 0;
//        int zeros = 0;
//        while(decodedMessage[i] == 0){
//            zeros++;
//            i++;
//        }
//
//        byte[] decodedMessageWithRemovedZeros = new byte[decodedMessage.length - zeros];
//        System.arraycopy(decodedMessage, zeros, decodedMessageWithRemovedZeros, 0, decodedMessage.length - zeros);
//
//        return decodedMessageWithRemovedZeros;
//    }


    public BigInteger getG() {
        return g;
    }

    public BigInteger getX() {
        return x;
    }

    /**
     * @return {p,q}
     */
    private BigInteger[] generatePQ() {
        BigInteger q = BigInteger.probablePrime(160, random);
        for (int i = 0; i < 4096; i++) {
            BigInteger m = BigInteger.ONE;
            while(m.bitLength() != 1024){
                m = new BigInteger(1024, random);
            }
            BigInteger mr = m.mod(q.multiply(BigInteger.TWO));
            BigInteger p = (m.subtract(mr)).add(BigInteger.ONE);
            if (p.isProbablePrime(1)){
                return new BigInteger[]{p,q};
            }
        }
        return null;
    }

    private BigInteger calculateG() {
//        BigInteger h = new BigInteger(1024, random).add(BigInteger.TWO).mod(p.subtract(new BigInteger("3")));
        BigInteger h = BigInteger.TWO;
        BigInteger g = h.modPow(p.subtract(BigInteger.ONE).divide(q),p);

        while(g.compareTo(BigInteger.ONE) == 0){
            h = h.add(BigInteger.ONE);
            g = h.modPow(p.subtract(BigInteger.ONE).multiply(q.modInverse(p)),p);
        }
        return g;
    }

    private BigInteger chooseX() {
        BigInteger x = BigInteger.ZERO;
        while(x.equals(BigInteger.ZERO)) {
            x = new BigInteger(160, random).mod(q);
        }
        return x;
    }

    private BigInteger calculateY() {
        BigInteger y = g.modPow(x,p);
        return y;
    }

    private BigInteger HashBytes(byte[] message) {
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

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getY() {
        return y;
    }
}
