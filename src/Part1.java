
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Part1 {
    // contains code demonstrating part 1 of the coursework
    //
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while(true) {

            System.out.println("Enter a number corresponding to the following options: \n" +
                    "1 : simple example of RSA encryption on a string\n" +
                    "2 : simple example of RSA brute-force attack\n" +
                    "3 : brute-force attack using factorisation against RSA\n" +
                    "4 : enter your own message to test RSA\n" +
                    "5 : enter your own parameters to test brute force\n" +
                    "WARNING: only completes in sensible amounts of time with < 8 characters\n" +
                    "6 : test random number generator (modified lcr method) for ints between 0 and 9 over a million iterations\n" +
                    "7 : test sending a signed message to verify sender\n" +
                    "8 : input own message to sign and then send to a user\n" +
                    "0 : exit");
            boolean valid = false;
            int in = 0;
            while(!valid) {
                try {
                    System.out.println("Choose your option: ");
                    in = input.nextInt();
                    valid = true;
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid input");
                    in = 0;
                    valid = false;
                    input = new Scanner(System.in);
                }
            }
            input = new Scanner(System.in);
            if (in == 1) {
                String myString = "Hello Bob, it's Alice here! Just messaging to let you know that my implementation is working.";
                simpleExample(myString);
            } else if (in == 2) {
                String myString = "Hi!";
                simpleBruteForce(myString);

            } else if (in == 3) {
                String myString = "Hello";
                factoringBruteForce(myString);
            } else if (in == 4) {
                simpleExample(input.nextLine());
            } else if (in == 5) {
                factoringBruteForce(input.nextLine());
            } else if (in == 6) {
                testRNG();
            } else if (in == 7) {
                testSign("Hello, it's me, Alice, but I have to sign this message in case you suspect I'm not who I say I am.");
            } else if (in == 8) {
                testSign(input.nextLine());
            } else if (in == 0) {
                System.out.println("Exiting.");
                break;

            } else {
                System.out.println("Please enter a valid input.");
            }
        }

    }

    public static void testRNG(){
        myRandom mr = new myRandom();
        int[] counts = new int[10];
        long total = 0;
        for(int i = 0; i < 1000000; i++) {
            int j = (int) mr.lcg(10);
            counts[j] ++;
            total += (j);
        }
        System.out.println((float)total/1000000);
        for(int i = 0; i < 10; i++) {
            System.out.println(i + " " + counts[i]);
        }
    }

    public static void simpleExample(String message){

        System.out.println("First, Alice and Bob must generate their own public and private keys- out of two large, random primes each.");
        System.out.println("");

        MyRSA alice = new MyRSA(512);
        MyRSA bob = new MyRSA(512);
        System.out.println("They keep their private keys to themselves.");
        System.out.println("Then, they each exchange each-other's public keys.");
        BigInteger[] aPubKey = alice.pubKey();
        BigInteger[] bPubKey = bob.pubKey();
        System.out.println("Finally, Alice and Bob can communicate by encrypting the messages they wish to send with the public key of the recipient.");
        System.out.println("Alice wishes to send the message to bob: ");
        System.out.println(message);
        System.out.println("She encrypts it: ");

        BigInteger[] c = MyRSA.encrypt(MyRSA.messageToBigInt(message), bPubKey);
        System.out.println(MyRSA.outToString(c));

        System.out.println("Then she sends it to Bob, who is able to decrypt it with his private key to get:");
        String m = MyRSA.outToString(bob.decrypt(c));
        System.out.println(m);
        System.out.println();

    }

    public static void simpleBruteForce(String message){
        //this will demonstrate an example in which charlie may use attack methods to break the encryption of the message.
        //methods covered here will be a brute-force attack and an impostor attack.
        System.out.println("In this example, Charlie will attempt to access the message Alice is sending to Bob.");
        System.out.println("In this example Charlie has no common sense, and wanted to calculate the private key by simply checking every odd value.");
        System.out.println("Alice and Bob communicate as usual, generating their private and public keys and sharing them.");
        System.out.println("Only this time, Charlie is in the middle, and able to intercept their communications.");
        System.out.println("Generating Alice's public/private keys:");
        MyRSA alice = new MyRSA(8);
        System.out.println();
        System.out.println("Generating Bob's public/private keys:");

        MyRSA bob = new MyRSA(8);
        System.out.println();

        System.out.println("Charlie now has access to the public key of Bob, and when Alice tries to send the message: ");

        System.out.println(message);
        System.out.println("Charlie can now attempt to guess the value contained in the data.");
        System.out.println("Because the message being sent is very small, and is not padded, a simple guessing brute-force can be performed.");

        BigInteger[] enc = MyRSA.encrypt(MyRSA.messageToBigInt(message), bob.pubKey());
        BigInteger privKey[] = bob.pubKey();
        privKey[0] = BigInteger.valueOf(3);
        BigInteger[] bruteForce = MyRSA.decrypt(enc, privKey);
        int tries = 1;
        while(!MyRSA.outToString(bruteForce).equals(message) || tries < 200000){
            privKey[0] = privKey[0].add(BigInteger.valueOf(2));
            bruteForce = MyRSA.decrypt(enc, privKey);

            tries ++;

        }



        System.out.println("Charlie manages to guess: \"" + MyRSA.outToString(bruteForce) + "\" in " + tries + " tries");
        System.out.println("When his method is by guessing every odd number from 3 onwards");
        System.out.println("\n\n");

        //given the public key of the recipient we want to be able to brute-force the answer from the public key
        //we do this by guessing n?

    }

    public static void factoringBruteForce(String message){
        System.out.println("Charlie will now attempt to guess Alice's message to Bob by factoring n until he discovers p and q for a small message.");
        System.out.println("This is significantly faster than simply bruteforce guessing every possible value that d might be for the private key, \n as p and q will be far smaller than the potential value of the private key.");
        System.out.println("However, it is still significantly slow and only a worthwhile idea for relatively small values of p and q");

        System.out.println("Generating Alice's public/private keys:");
        MyRSA alice = new MyRSA(message.length() * 4);
        System.out.println();
        System.out.println("Generating Bob's public/private keys:");
        MyRSA bob = new MyRSA(message.length() * 4);
        System.out.println();

        BigInteger[] cipherText = MyRSA.encrypt(MyRSA.messageToBigInt(message), bob.pubKey());

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
        System.out.println(MyRSA.outToString(cipherText));
        System.out.println();
        System.out.println("Charlie guessed the generating factors: "+ p +" "+ cq + " in " + count + " tries");

        BigInteger totient = p.subtract(BigInteger.ONE).multiply( cq.subtract(BigInteger.ONE) );
        System.out.println("Charlie then calculates the totient, and then the private key d");
        System.out.println("As the modular inverse of the public key with respect to the totient provides the private key");

        System.out.println("Calculated totient: " + totient);
        BigInteger d = bob.pubKey()[0].modInverse(totient);

        System.out.println("Calculated private key exponent d: " + d);
        BigInteger[] privKey = {d, n};
        System.out.println("Charlie manages to extract the message: " + MyRSA.outToString(MyRSA.decrypt(cipherText, privKey)));
        System.out.println();

    }

    public static void testSign(String message){
        System.out.println("Setting up RSA public and private keys for Alice and Bob:");

        MyRSA alice = new MyRSA(512);
        MyRSA bob = new MyRSA(512);

        System.out.println("Alice wants to send the following message to Bob: ");
        System.out.println(message);
        System.out.println("She wants to make sure that Bob knows it's her, however.");
        System.out.println("To do this, she creates a secure hash of her message, and raises it to the power of her private key mod n.");
        System.out.println("She sends this, along with her encrypted message to Bob so that he can decrypt the hash by raising it to the value of her public key mod n");
        System.out.println("Next, Bob hashes the message he has decrypted using his private key, and compares it to the hash he has from Alice.\nIf they match, it's definitely come from Alice as only she knows her private key.");
        BigInteger[] signature = alice.signMessage(message);
        bob.decrypt(alice.encrypt(MyRSA.messageToBigInt(message), bob.pubKey()));
        bob.verifySignature(message, signature, alice.pubKey());
        System.out.println(bob.verifySignature(message, signature, alice.pubKey()));

        System.out.println("Bob checks if they match: ");
        System.out.println(MyRSA.verifySignature("Hello bob", alice.signMessage("Hello bob"), alice.pubKey()));

    }

}
