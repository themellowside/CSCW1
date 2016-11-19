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
        Server server = new Server(512);
        Client a = new Client("Alice", 512);
        Client b = new Client("Bobby", 512);
        server.addUser(a.getName(),a.pubKey());
        server.addUser(b.getName(), b.pubKey());

        //A asks S for B's public key
        //S sends A B's public key and a signature
        //a SIGNS the message, and the server checks the signature is correct before replying
        Pair<Object[], BigInteger[]> bData = server.getUserPKey("Bobby", a.signMessage("Bobby"), a.pubKey());
        //contains b, signed b, and b's public key
        //check that the signature is correct
        if(a.verifySignature((String) bData.getKey()[0], (BigInteger) bData.getKey()[1], server.pubKey())){
            System.out.println("Server signature is correct, proceeding to send b a nonce.");
        }

        String[] encryptedNonceA = a.generateNonce(bData.getValue());
        //a now sends this to b, who decrypts both the message and the signature, but cannot validate it,
        //b proceeds to ask S for the public key of a
        Pair<Object[], BigInteger[]> aData = server.getUserPKey("Alice", a.signMessage("Alice"), a.pubKey());
        //contains b, signed b, and b's public key
        //check that the signature is correct
        if(b.verifySignature((String) aData.getKey()[0], (BigInteger) aData.getKey()[1], server.pubKey())){
            System.out.println("Server signature is correct, proceeding to validate A's nonce.");
        }

        //b decrypts the message and verifies the signature
        String aNonceValue = b.decrypt(encryptedNonceA[0]);
        String aNonceSignature = b.decrypt(encryptedNonceA[1]);
        if(b.validateNonce(aNonceValue, new BigInteger(aNonceSignature.getBytes()), aData.getValue())){
            System.out.println("A's hashed nonce matches the signature, proceeding to reply");
        }


        //b then encrypts the nonce he's worked out, a new nonce, and b's signature with a's public key, and sends it to A to validate
        String encryptedReplyNonceA = b.encrypt(aNonceValue, aData.getValue());
        String encryptedReplyNonceSignatureA = b.encrypt(new String(b.signMessage(aNonceValue).toByteArray()), aData.getValue());
        String[] encryptedNonceB = b.generateNonce(aData.getValue());


        //a decrypts all of these values, validates the signature, checks the nonce he sent originally has the same value as the nonce b decrypted,
        //and then replies with an encrypted nonce, and a hash of that as the signature.


        //A sends B the unencrypted nonce to prove it's been decrypted

    }

}
