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
        nonce = myRandom.randomBigInt(16).toString();
        String message = encrypt(nonce.toString(), rPubKey);
        String signature = encrypt(signMessage(nonce.toString()).toString(), rPubKey);

        String[] nonce = {message, signature};
        return nonce;
    }

    public boolean validateNonce(String proof, BigInteger signature, BigInteger[] pubKey){
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
