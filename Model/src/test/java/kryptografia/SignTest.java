package kryptografia;

import java.security.interfaces.DSAPublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SignTest {


    Random rand = new Random();
    Alice alice;
    Bob bob;

    @BeforeEach
    void setUp() {
        alice = new Alice();
        bob = new Bob(alice.getP(), alice.getQ(), alice.getG(), alice.getY());

    }


    @RepeatedTest(20)
    void testPQGeneration() {
        var q = alice.getQ();
        var p = alice.getP();
        var g = alice.getG();
        var x = alice.getX();
        assertTrue(p.subtract(BigInteger.ONE).divideAndRemainder(q)[1].equals(BigInteger.ZERO));
    }

    @Test
    void hashTest() {
        byte[] message = {0,2,3,2,2,3,1,1,1,1,1,2,2,2};
        BigInteger a = bob.HashBytes(message);

        byte[] message2 = {0,2,3,2,2,3,1,1,1,1,1,2,2,2};
        BigInteger b = bob.HashBytes(message2);

        assertEquals(a,b);

        byte[] message3 = {0,2,3,2,2,3,1,1,1,1,1,2,2,2,1,1,1};
        BigInteger c = bob.HashBytes(message3);
        assertNotEquals(a,c);
    }

    @RepeatedTest(5)
    void testSign() {
        // positive case
        byte[] message = {0,2,3,2,2,3,1,1,1,1,1,2,2,2};
        var sign = alice.sign(message);
        assertTrue(bob.verifySignature(sign[0], sign[1], message));

        // negative case

        byte[] anotherMessage = {0,2,3,3,4,2,2};
        assertFalse(bob.verifySignature(sign[0], sign[1], anotherMessage));

    }

    @RepeatedTest(1)
    void aliceParamsTest() {
        Alice alice = new Alice();
    }



//    @RepeatedTest(100)
//    void test() {
//
//        // generate random string
//        Bob bob = new Bob();
////        Alice alice = new Alice(bob.getPrime(), bob.getAlfa(), bob.getBobsPublicKey());
////        bob.computeMaskingKey(alice.getAlicesPublicKey());
//
//
//        byte[] arr = new byte[100];
//        new Random().nextBytes(arr);
//        System.out.println("block to encrypt: " + Arrays.toString(arr));
//        List<byte[]> encrypted = alice.encode(arr);
//        System.out.println("cryptogram: ");
//        for (var part: encrypted) {
//            System.out.println(Arrays.toString(part));
//        }
//
//
////        byte[] decrypted = bob.decode(encrypted);
////        System.out.println("Decrypted: ");
////        System.out.println(Arrays.toString(decrypted));
////
////        assertArrayEquals(arr, decrypted);
//
//    }
}
