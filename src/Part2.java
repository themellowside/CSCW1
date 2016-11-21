import javafx.util.Pair;

import java.math.BigInteger;

/**
 * Created by Thomas on 16/11/2016.
 */
public class Part2 {
    //contains code demonstrating part 2 of the coursework
    //class that acts as a server who distributes public keys on behalf of others
    //A asks S for B's public key
    //S sends A B's public key and a signature
    //A sends B a random value that's encrypted with B's pub key
    //B asks S for A's public key
    //S sends B A's public key and a signature
    //B sends A a random value encrypted with A's public key, as well as the decrypted nonce proving that B decrypted it
    //A sends B the unencrypted nonce to prove it's been decrypted





    public static void main(String[] args) {
        System.out.println("Generating private and public keys for Server:");
        Server server = new Server(512);
        System.out.println("Generating private and public keys for Alice:");

        Client a = new Client("Alice", 512);
        System.out.println("Generating private and public keys for Bobby:");

        Client b = new Client("Bobby", 512);

        System.out.println();
        System.out.println("Adding users to server directory");
        server.addUser(a.getName(),a.pubKey());
        server.addUser(b.getName(), b.pubKey());

        //A asks S for B's public key
        //S sends A B's public key and a signature
        //a SIGNS the message, and the server checks the signature is correct before replying
        System.out.println("Alice asks the server for the public key of user \"Bobby\"");

        Pair<Object[], BigInteger[]> bData = server.getUserPKey("Bobby", a.signMessage("Bobby"), a.pubKey());
        //contains b, signed b, and b's public key
        //check that the signature is correct
        System.out.println("Alice attempts to verify the server's signature");
        if(RSAUser.verifySignature((String) bData.getKey()[0], (BigInteger) bData.getKey()[1], server.pubKey())){
            System.out.println("Server signature is correct, proceeding to generate a nonce to send to Bobby.");

            Object[] encryptedNonceA = a.generateNonce(bData.getValue());
            System.out.println("Sending nonce to Bobby.");
            //a now sends this to b, who decrypts both the message and the signature, but cannot validate it,
            //b proceeds to ask S for the public key of a
            System.out.println("Bobby asks the server for the public key of user Alice.");

            Pair<Object[], BigInteger[]> aData = server.getUserPKey("Alice", b.signMessage("Alice"), b.pubKey());
            //contains b, signed b, and b's public key
            //check that the signature is correct
            System.out.println("Bobby attempts to verify the Server's signature.");

            if(RSAUser.verifySignature((String) aData.getKey()[0], (BigInteger) aData.getKey()[1], server.pubKey())){
                System.out.println("Server signature is correct, proceeding to validate A's nonce.");
                System.out.println("Now Bobby decrypts the message, and is able to validate the signature as he has Alice's public key.");
                System.out.println("Bobby keeps hold of the nonce's decrypted value for later.");
                String aNonceValue = b.decrypt((String)encryptedNonceA[0]); //decrypt nonce
                String[] aNonceSignatures = (String[]) encryptedNonceA[1];
                String aNonceSignature = "";
                for(String s : aNonceSignatures){
                    aNonceSignature += b.decrypt(s);
                }
                System.out.println( aNonceSignature);
                System.out.println( a.signMessage(aNonceValue));
                // System.out.println("encrypted nonce is: " + encryptedNonceA[0]);
                //System.out.println("encrypted signature is: " + encryptedNonceA[1]);
                //System.out.println("aNonceValue is: "  + aNonceValue);
                //System.out.println("aNonceSignature: " +new String(aNonceSignature));

                //System.out.println(a.signMessage(aNonceValue));


                //b verifies the signature
                System.out.println("Bobby now attempts to validate the signature.");
                if(RSAUser.verifySignature(aNonceValue, new BigInteger(aNonceSignature), aData.getValue())){
                    System.out.println("Alice''s signature is correct, proceeding to generate a nonce to send to Alice.");
                    System.out.println("Bobby now sends Alice a new nonce, and the value he got for decrypting hers.");

                    Object[] encryptedNonceB = b.generateNonce(aData.getValue());
                    //b encrypts the nonce, and his decrypted message, and sends them to a with a signature
                    //a decrypts the nonce, signature and replied nonce, validates the signature, and then checks the replied nonce is correct
                    System.out.println("Alice decrypts Bobby's new nonce, signature and the old nonce.");

                    String bNonceValue = a.decrypt((String)encryptedNonceB[0]); //decrypt nonce
                    String[] bNonceSignatures = (String[]) encryptedNonceB[1];
                    String bNonceSignature = "";
                    for(String s : bNonceSignatures){
                        bNonceSignature += a.decrypt(s);
                    }
                    String encryptedReplyNonceA = RSAUser.encrypt(aNonceValue, aData.getValue());

                    System.out.println("Alice attempts to verify B's signature");
                    if(RSAUser.verifySignature(bNonceValue, new BigInteger(bNonceSignature), bData.getValue())){
                        System.out.println("B's signature is correct, proceeding to check nonce is correct");
                        System.out.println("Alice attempts to verify B's value of her nonce.");
                        if(a.validateNonce(encryptedReplyNonceA)) {
                            System.out.println("Alice's nonce matches, proceeding to send nonce back to B.");
                            String encryptedReplyNonceB = RSAUser.encrypt(bNonceValue, bData.getValue());
                            BigInteger signedReplyNonceB = a.signMessage(bNonceValue);
                            //if nonce is correct, encrypt nonce reply and nonce reply signature and now b and a can trust one another(?)
                            //in here alice needs to verify a signature so this still needs changing
                            if(b.verifySignature(bNonceValue, signedReplyNonceB, aData.getValue())) {
                                System.out.println("B's signature is correct, proceeding to check nonce value.");
                                if (b.validateNonce(encryptedReplyNonceB)) {
                                    System.out.println("B's nonce matches, communication is now established.");
                                }
                            }
                        }
                    }
                }
            }
        }


        //now a sends the nonce to b

        //b decrypts the message


        //b generates a new nonce and signature



        //b sends the old nonce, and the new nonce signed, both encrypted, to a
        //a decrypts the old nonce, checks it is the correct value
        //a decrypts the new nonce, checks the signature
        //a signs and encrypts the new nonce, and sends it to b

        //communication is now established
    }

}
