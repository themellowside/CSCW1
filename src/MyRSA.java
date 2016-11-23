import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by Thomas on 22/11/2016.
 */
public class MyRSA {

    private BigInteger e;
    private BigInteger d;
    private BigInteger n;

    MyRSA(int bitLength){

        BigInteger p = myRandom.randomPrime(bitLength);
        BigInteger q = myRandom.randomPrime(bitLength);
        System.out.println("P: " + p + " Bitlength: " + p.bitLength());
        System.out.println("Q: " + q + " Bitlength: " + q.bitLength());
        //calculate n from p and q
        //n is the modulus for the public and private keys
        setup(p, q);


    }

    MyRSA(BigInteger p, BigInteger q){
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

    public static BigInteger[] encrypt(BigInteger[] message, BigInteger[] pkey){
        BigInteger[] c = new BigInteger[message.length];

        for(int i = 0; i < message.length; i++){
            c[i] = message[i].modPow(pkey[0], pkey[1]);
        }
        return c;
    }



    public BigInteger[] decrypt(BigInteger[] cipher){
        BigInteger[] m = new BigInteger[cipher.length];
        for(int i = 0 ; i < cipher.length; i++){
            m[i] = cipher[i].modPow(d, n);
        }
        return m;
    }

    public static BigInteger[] decrypt(BigInteger[] cipher, BigInteger[] privkey){
        BigInteger[] m = new BigInteger[cipher.length];
        for(int i = 0 ; i < cipher.length; i++){
            m[i] = cipher[i].modPow(privkey[0], privkey[1]);
        }
        return m;
    }


    public BigInteger[] signMessage(String m){
        BigInteger[] signature = byteToBigInt(hash(m));
        //System.out.println("Printing hash for string: "+ m);
        /*for(int i = 0; i < signature.length; i++) {
            System.out.print(signature[i] + " ");
        }
        System.out.println("eom");*/
        return decrypt(signature);
    }

    public static byte[] hash(String mToHash){
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(mToHash.getBytes()); // Change this to "UTF-16" if needed
            byte[] hash = md.digest();
            //System.out.println("printing message " + message + " hash");
            //System.out.println(new String(hash));

            return hash;
        }catch(Exception e){
            return null;
        }
    }

    public static boolean verifySignature(String message, BigInteger[] signature, BigInteger[] pubKey){
        //BigInteger[] messageHash = new BigInteger[message.length()];

        BigInteger[] messageHash = byteToBigInt(hash(message));
        /*System.out.print("Message hash: ");

        for(int i = 0; i < messageHash.length; i++) {
            System.out.print(messageHash[i] + " ");
        }

        System.out.println(" eom");
        System.out.print("Signature given pre-decryption: ");
        for(int i = 0; i < signature.length;i ++){
            System.out.print(signature[i] + " ");
        }
        System.out.println(" eom");
        System.out.print("Signature given post-decryption: ");
        */
        signature = encrypt(signature, pubKey); //we call encrypt as it's the same code as the process for this - modpow e, n
        /*for(int i = 0; i < signature.length;i ++){
            System.out.print(signature[i] + " ");
        }
        System.out.println(" eom");
        */
        //if(signature.length != messageHash.length){
        //    return false;
        //}



        for(int i = 0; i < signature.length; i ++){
            if(!signature[i].equals(messageHash[i])){
                return false;
            }
        }

        return true;
    }

    public BigInteger[] pubKey(){
        BigInteger[] arr = {e, n};
        return arr;
    }

    public static String outToString(BigInteger[] message){
        String out = "";
        for(int i = 0; i < message.length; i++) {
            out += new String(message[i].toByteArray());
        }
        return out;
    }

    public static BigInteger[] messageToBigInt(String message){
        BigInteger[] in = new BigInteger[message.length()];

        for(int i = 0; i < message.length(); i ++){
            String subStr = message.substring(i, i+1);
            //System.out.println(subStr);
            //System.out.println(new BigInteger(subStr.getBytes()));
            in[i] = new BigInteger(subStr.getBytes());
        }

        return in;
    }

    public static BigInteger[] byteToBigInt(byte[] in){
        BigInteger[] out = new BigInteger[in.length];
        for(int i = 0; i < in.length; i ++){
            out[i] = new BigInteger(Arrays.copyOfRange(in, i, i+1)).abs(); //take absolute because it's broken for negative values. it's hacky but i couldn't figure it out otherwise
        }
        return out;
    }
}
