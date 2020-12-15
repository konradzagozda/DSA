package kryptografia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BobTest {


    Random rand = new Random();
    Bob bob;
    Alice alice;

    @BeforeEach
    void setUp() {
        bob = new Bob();
        alice = new Alice(bob.getPrime(), bob.getAlfa(), bob.getBobsPublicKey());
    }


    @RepeatedTest(100)
    void test() {

        // generate random string
        Bob bob = new Bob();
        Alice alice = new Alice(bob.getPrime(), bob.getAlfa(), bob.getBobsPublicKey());
        bob.computeMaskingKey(alice.getAlicesPublicKey());


        byte[] arr = new byte[100];
        new Random().nextBytes(arr);
        System.out.println("block to encrypt: " + Arrays.toString(arr));
        List<byte[]> encrypted = alice.encode(arr);
        System.out.println("cryptogram: ");
        for (var part: encrypted) {
            System.out.println(Arrays.toString(part));
        }


        byte[] decrypted = bob.decode(encrypted);
        System.out.println("Decrypted: ");
        System.out.println(Arrays.toString(decrypted));

        assertArrayEquals(arr, decrypted);

    }
}
