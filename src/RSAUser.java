import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tom on 09/10/2016.
 */
public class RSAUser {

    /*
    *
    * Further exploration:
    * Padding schemes in order to prevent insecure ciphertexts m = 0 results in c = 0 for example
    *
    * */

    private BigInteger e;
    private BigInteger d;
    private BigInteger n;

    RSAUser(int bitLength){

        BigInteger p = myRandom.randomPrime(bitLength);
        BigInteger q = myRandom.randomPrime(bitLength);
        System.out.println("P: " + p + " Bitlength: " + p.bitLength());
        System.out.println("Q: " + q + " Bitlength: " + q.bitLength());
        //calculate n from p and q
        //n is the modulus for the public and private keys
        setup(p, q);


    }

    RSAUser(BigInteger p, BigInteger q){
        setup(p, q);
    }

    private void setup(BigInteger p, BigInteger q){
        n = p.multiply(q);
        //calculate the totient
        //
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        System.out.println("n: " + n);
        System.out.println("phi: " + totient);
        //choose an integer e such that:
        //1 < e < totient
        //and e is coprime to the totient
        //(e and the totient must share no factors other than 1)
        //e is the public key exponent
        e = myRandom.randomPrime(totient.bitLength() - 1);

        while(totient.mod(e).equals(BigInteger.ZERO)){

            e = myRandom.randomPrime(totient.bitLength() - 1);

        }
        System.out.println("e: " + e);
        System.out.println("bitlength for e: " + e.bitLength());
        //basically, e has to share no common factors other than 1, and be less than the totient
        //calculate d to satisfy the congruence relation de === 1 (mod (totient))
        //d is the private key exponent
        //d=e^(-1) mod [(p-1)x(q-1)]

        d = e.modInverse(totient);

        System.out.println("d: " + d);
        System.out.println("bitlength for d: " + d.bitLength());

        //public key is made of n and the exponent e
        //private key is made of modulus n and exponent d
    }

    public static String encrypt(String m, BigInteger[] pubKey){
        //return it as a string because it's just easier to handle that way
        //take m as a byteArray and convert it into a biginteger so modPow is easy to apply
        BigInteger msg = new BigInteger(m.getBytes());

        return msg.modPow(pubKey[0], pubKey[1]).toString();
        //ciphertext = message^e mod n

    }

    public String decrypt(String c){
        //convert string to biginteger
        BigInteger ctxt = new BigInteger(c);
        //message = ciphertext^d mod n
        BigInteger msg = ctxt.modPow(d, n);
        //convert biginteger to string - has to be through bytearray or it's just going to print numbers instead of original message
        return new String(msg.toByteArray());
    }

    public static String decrypt(String c, BigInteger[] privKey){
        BigInteger ctxt = new BigInteger(c);
        //message = ciphertext^d mod n
        BigInteger msg = ctxt.modPow(privKey[0], privKey[1]);
        //convert biginteger to string - has to be through bytearray or it's just going to print numbers instead of original message
        return new String(msg.toByteArray());
    }


    public BigInteger[] pubKey(){
        BigInteger[] arr = {e, n};
        return arr;
    }

    public BigInteger signMessage(String message){
        String hash = hash(message);
        //hash the message
        //raise it to the power of d(modulo n) and attach it to the message
        //(same operation as decryption)
        //System.out.println(hash);
        BigInteger hashBytes = new BigInteger(hash.getBytes());
        return hashBytes.modPow(d, n);
    }

    public static Boolean verifySignature(String message, BigInteger signature, BigInteger[] pk){
        //hash the message
        //
        //raise the signature to e modulo n (which is the sender's public key)
        //
        //System.out.println("Verification message, signature");
        //System.out.println(message);
        //System.out.println(signature);
        //System.out.println();
        String messageHash = hash(message);

        BigInteger sigHash = signature.modPow(pk[0], pk[1]);

        //System.out.println();
        //System.out.println(sigHash);
        //System.out.println(new BigInteger(messageHash.getBytes()));
        //System.out.println();
        //*/
        return sigHash.equals(new BigInteger(messageHash.getBytes()));

        //compare the hashed message to the signature after this operation
        //if they're the same, return true
        //else return false- sender is a fraud
    }

    public static String hash(String message){
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(message.getBytes()); // Change this to "UTF-16" if needed
            byte[] hash = md.digest();
            //System.out.println("printing message " + message + " hash");
            //System.out.println(new String(hash));
            String hashString = new String(hash);
            if(hashString.length() >= 128){
                return hashString.substring(0, 128);
            }else {
                return hashString;
            }
        }catch(Exception e){
            return null;
        }
    }

}
