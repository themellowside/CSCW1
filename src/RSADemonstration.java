import java.math.BigInteger;

public class RSADemonstration {

    public static void main(String[] args) {
        simpleExample();

    }



    public static void simpleExample(){
        String myString = "Hello Bob, it's Alice here! Just messaging to let you know that my implementation is working.";

        System.out.println("First, Alice and Bob must generate their own public and private keys- out of two large, random primes each.");
        System.out.println("");

        RSAUser alice = new RSAUser(512);
        RSAUser bob = new RSAUser(512);
        System.out.println("They keep their private keys to themselves.");
        System.out.println("Then, they each exchange each-other's public keys.");
        BigInteger[] aPubKey = alice.pubKey();
        BigInteger[] bPubKey = bob.pubKey();
        System.out.println("Finally, Alice and Bob can communicate by encrypting the messages they wish to send with the public key of the recipient.");
        System.out.println("Alice wishes to send the message to bob: ");
        System.out.println(myString);
        System.out.println("She encrypts it: ");

        String c = RSAUser.encrypt(myString, bPubKey);
        System.out.println(c);

        System.out.println("Then she sends it to Bob, who is able to decrypt it with his private key:");
        String m = bob.decrypt(c);
        System.out.println(m);
    }

    public static void hackedExample(){
        //this will demonstrate an example in which charlie may use attack methods to break the encryption of the message.
        //methods covered here will be a brute-force attack and an impostor attack.
        System.out.println("In this example, Charlie will attempt to access the message Alice is sending to Bob.");
        //given the public key of the recipient we want to be able to brute-force the answer from the public key
        //we do this by guessing n?

    }

}
