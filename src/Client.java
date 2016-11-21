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

    public Object[] generateNonce(BigInteger[] rPubKey){
        BigInteger nonceBigInt = myRandom.randomBigInt(16);
        nonce = nonceBigInt.toString();
        String message = encrypt(nonce, rPubKey);
        String signature = signMessage(nonce).toString();
        //System.out.println(signature.length());
        //System.out.println("aNonceSignature should be: " + signature.substring(0, 128));
        //System.out.println("aNonceSignature encrypted: " + encrypt(signature.substring(0, 128), rPubKey));
        //System.out.println("aNonceValue encrypted: " + message);
        //System.out.println("aNonceValue: " + nonce);
        String[] encSig = new String[(int) Math.floor(signature.length()/128) + 1];
        for(int i = 0; i <= Math.floor(signature.length()/128) ; i++){
            if((i+1)*128 < signature.length()) {
                encSig[i] = encrypt(signature.substring(i * 128, (i + 1) * 128), rPubKey);
            }else {
                encSig[i] = encrypt(signature.substring(i * 128), rPubKey);
            }
        }
        Object[] nonce = {message, encSig};
        return nonce;
    }

    public boolean validateNonce(String proof){
        return decrypt(proof).equals(nonce);
    }




}
