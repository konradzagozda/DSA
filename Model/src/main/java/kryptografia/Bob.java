package kryptografia;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bob {
    private Random random;

    private BigInteger prime;
    private BigInteger alfa;
    private BigInteger bobsPrivateKey;
    private BigInteger bobsPublicKey;
    private BigInteger maskingKey;
    private BigInteger invMaskingKey;


    public Bob() {
        random = new Random();
        prime = BigInteger.probablePrime(256, random);
        alfa = BigInteger.probablePrime(255, random);

        bobsPrivateKey = BigInteger.probablePrime(255, random);
        bobsPublicKey = alfa.modPow(bobsPrivateKey, prime);
    }

    public BigInteger getPrime() {
        return prime;
    }

    public BigInteger getAlfa() {
        return alfa;
    }

    public BigInteger getBobsPrivateKey() {
        return bobsPrivateKey;
    }

    public BigInteger getBobsPublicKey() {
        return bobsPublicKey;
    }

    public BigInteger computeMaskingKey(BigInteger alicesPublicKey) {
        BigInteger maskingKey = alicesPublicKey.modPow(bobsPrivateKey, prime);
        this.maskingKey = maskingKey;
        this.invMaskingKey = maskingKey.modInverse(prime);
        return maskingKey;
    }

    public byte[] decrypt(byte[] block) {
        var bigIntBlock = new BigInteger(1,block);

        var bigIntResult = bigIntBlock.multiply(invMaskingKey).mod(prime);
        var result =  bigIntResult.toByteArray();

        if (result.length > 16){
            byte[] tmp = new byte[16];
            System.arraycopy(result,result.length-16,tmp,0,16);
            result = tmp;
        }

        if (result.length < 16) {
            byte[] tmp = new byte[16];
            System.arraycopy(result,0, tmp,16-result.length, result.length);
            result = tmp;
        }

        return result;
    }

    public byte[] decode(List<byte[]> encodedBlocks) {
        byte[] decodedMessage = new byte[encodedBlocks.size() * 16];

        for (int i = 0; i < encodedBlocks.size(); i++) {
            byte[] decryptedBlock = decrypt(encodedBlocks.get(i));
            System.arraycopy(decryptedBlock,0,decodedMessage,16*i,16);
        }


        // remove leading zeros
        int i = 0;
        int zeros = 0;
        while(decodedMessage[i] == 0){
            zeros++;
            i++;
        }

        byte[] decodedMessageWithRemovedZeros = new byte[decodedMessage.length - zeros];
        System.arraycopy(decodedMessage, zeros, decodedMessageWithRemovedZeros, 0, decodedMessage.length - zeros);

        return decodedMessageWithRemovedZeros;
    }

}
