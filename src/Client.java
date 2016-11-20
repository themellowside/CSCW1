import java.math.BigInteger;

/**
 * Created by tom on 17/11/2016.
 */
public class Client extends RSAUser{
    private String name;
    private String nonce;
    //class to wrap the demonstration and make it a little easier

    Client(String name){
        super(512);
        this.name = name;

    }

    Client(String name, int l){
        super(l);
        this.name = name;

    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String[] generateNonce(BigInteger[] rPubKey){
        BigInteger nonceBigInt = myRandom.randomBigInt(16);
        nonce = nonceBigInt.toString();
        String message = encrypt(nonce, rPubKey);
        String signature = signMessage(nonce).toString();
        System.out.println(signature.length());
        System.out.println("aNonceSignature should be: " + signature.substring(0, 128));
        System.out.println("aNonceSignature encrypted: " + encrypt(signature.substring(0, 128), rPubKey));
        System.out.println("aNonceValue encrypted: " + message);
        System.out.println("aNonceValue: " + nonce);

        String[] nonce = {message, encrypt(signature.substring(0, 128), rPubKey)};
        return nonce;
    }

    public boolean validateNonce(String proof, BigInteger signature, BigInteger[] pubKey){
        System.out.println("validating signature: " + signature);
        if(verifySignature(proof, signature, pubKey)) {

            if (proof.equals(nonce)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }




}
