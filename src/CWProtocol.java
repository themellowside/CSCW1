/**
 * Created by Thomas on 16/11/2016.
 */
public class CWProtocol {

    //class that acts as a server who distributes public keys on behalf of others
    //A asks S for B's public key
    //S sends A B's public key and a signature
    //A sends B a random value that's encrypted with B's pub key
    //B asks S for A's public key
    //S sends B A's public key and a signature
    //B sends A a random value encrypted with A's public key, as well as the decrypted nonce proving that B decrypted it
    //A sends B the unencrypted nonce to prove it's been decrypted



}
