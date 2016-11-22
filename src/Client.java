import java.math.BigInteger;

/**
 * Created by tom on 17/11/2016.
 */
public class Client extends MyRSA{
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

    public Object[] generateNonce(BigInteger[] rPubKey){
        BigInteger nonceBigInt = myRandom.randomBigInt(16);
        nonce = nonceBigInt.toString();
        BigInteger[] message = encrypt(messageToBigInt(nonce), rPubKey);
        BigInteger[] signature = signMessage(nonce);
        //System.out.println(signature.length());
        //System.out.println("aNonceSignature should be: " + signature.substring(0, 128));
        //System.out.println("aNonceSignature encrypted: " + encrypt(signature.substring(0, 128), rPubKey));
        //System.out.println("aNonceValue encrypted: " + message);
        //System.out.println("aNonceValue: " + nonce);

        BigInteger[][] nonce = {message, signature};
        return nonce;
    }

    public boolean validateNonce(BigInteger[] proof){
        return decrypt(proof).equals(nonce);
    }




}
