import java.math.BigInteger;

public class RSADemonstration {

    public static void main(String[] args) {
        //simpleExample();
        hackedExample();
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
        System.out.println("Alice and Bob communicate as usual, generating their private and public keys and sharing them.");
        System.out.println("Only this time, Charlie is in the middle, and able to intercept their communications.");
        System.out.println("Generating Alice's public/private keys:");
        RSAUser alice = new RSAUser(12);
        System.out.println("Generating Bob's public/private keys:");

        RSAUser bob = new RSAUser(12);

        System.out.println("Charlie now has access to the public key of Bob, and when Alice tries to send the message: ");
        String myString = "Bob";

        System.out.println(myString);
        System.out.println("Charlie can now attempt to guess the value contained in the data.");
        System.out.println("Because the message being sent is very small, and is not padded, a simple guessing brute-force can be performed.");
        String enc = RSAUser.encrypt(myString, bob.pubKey());
        BigInteger privKey[] = bob.pubKey();
        privKey[0] = BigInteger.valueOf(3);
        String bruteForce = RSAUser.decrypt(enc, privKey);
        int tries = 1;
        while(!bruteForce.equals(myString)){
            privKey[0] = privKey[0].add(BigInteger.valueOf(2));
            bruteForce = RSAUser.decrypt(enc, privKey);
            tries ++;
        }



        System.out.println("Charlie manages to guess: \"" + bruteForce + "\" in " + tries + " tries");
        System.out.println("When his method is by guessing every odd number from 3 onwards");

        //given the public key of the recipient we want to be able to brute-force the answer from the public key
        //we do this by guessing n?

    }

}
