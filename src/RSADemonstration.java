import java.math.BigInteger;
import java.util.Scanner;

public class RSADemonstration {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        /*while(true) {
            int in = input.nextInt();

            if (in == 1) {
                simpleExample();
            } else if (in == 2) {
                simpleBruteForce();
            } else if (in == 3) {
                factoringBruteForce();
            } else if (in == 4) {

            } else if (in == 0) {
                break;

            }
        }*/
        myRandom mr = new myRandom();
        long total = 0;
        for(int i = 0; i < 1000000; i++) {

            total += (mr.lcg(10));
        }
        System.out.print((float)total/1000000);

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
        System.out.println();

    }

    public static void simpleBruteForce(){
        //this will demonstrate an example in which charlie may use attack methods to break the encryption of the message.
        //methods covered here will be a brute-force attack and an impostor attack.
        System.out.println("In this example, Charlie will attempt to access the message Alice is sending to Bob.");
        System.out.println("In this example Charlie has no common sense, and wanted to calculate the private key by simply checking every odd value.");
        System.out.println("Alice and Bob communicate as usual, generating their private and public keys and sharing them.");
        System.out.println("Only this time, Charlie is in the middle, and able to intercept their communications.");
        System.out.println("Generating Alice's public/private keys:");
        RSAUser alice = new RSAUser(12);
        System.out.println();

        System.out.println("Generating Bob's public/private keys:");

        RSAUser bob = new RSAUser(12);
        System.out.println();

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
        System.out.println("\n\n");

        //given the public key of the recipient we want to be able to brute-force the answer from the public key
        //we do this by guessing n?

    }

    public static void factoringBruteForce(){
        System.out.println("Charlie will now attempt to guess Alice's message to Bob by factoring n until he discovers p and q for a small message.");
        System.out.println("This is significantly faster than simply bruteforce guessing every possible value that d might be for the private key, \n as p and q will be far smaller than the potential value of the private key.");
        System.out.println("However, it is still significantly slow and only a worthwhile idea for relatively small values of p and q");
        String message = "Bob";

        System.out.println("Generating Alice's public/private keys:");
        RSAUser alice = new RSAUser(12);
        System.out.println();
        System.out.println("Generating Bob's public/private keys:");
        RSAUser bob = new RSAUser(12);
        System.out.println();

        String cipherText = alice.encrypt(message, bob.pubKey());

        BigInteger n = bob.pubKey()[1];
        BigInteger cq = BigInteger.valueOf(3);
        int count = 1;
        while(!n.mod(cq).equals(BigInteger.ZERO)) {
            cq = cq.add(BigInteger.valueOf(2));
            count ++;
        }
        BigInteger p = n.divide(cq);
        System.out.println("Alice wishes to send the message: " + message + " to Bob.");
        System.out.println("Alice encrypts it, resulting in the cipherText:");
        System.out.println(cipherText);
        System.out.println();
        System.out.println("Charlie guessed the generating factors: "+ p +" "+ cq + " in " + count + " tries");

        BigInteger totient = p.subtract(BigInteger.ONE).multiply( cq.subtract(BigInteger.ONE) );
        System.out.println("Charlie then calculates the totient, and then the private key d");
        System.out.println("As the modular inverse of the public key with respect to the totient provides the private key");
        BigInteger d = bob.pubKey()[0].modInverse(totient);
        BigInteger[] privKey = {d, n};
        System.out.println("Charlie manages to extract the message: " + RSAUser.decrypt(cipherText, privKey));


    }

}
