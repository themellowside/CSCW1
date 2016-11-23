import java.math.BigInteger;

/**
 * Created by Thomas on 22/11/2016.
 */
public class Part1test {

    public static void main(String[] args) {
        MyRSA alice = new MyRSA(512);
        MyRSA bob = new MyRSA(512);

        BigInteger[] encrypted = alice.encrypt( MyRSA.messageToBigInt("hello"), bob.pubKey());
        BigInteger[] signature = alice.signMessage("hello");

        BigInteger[] decrypted = bob.decrypt(encrypted);
        System.out.println(MyRSA.outToString(decrypted));
        System.out.println(MyRSA.verifySignature(MyRSA.outToString(decrypted), signature, alice.pubKey()));
    }
}
