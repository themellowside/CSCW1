import javafx.util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Thomas on 16/11/2016.
 */
public class Server extends RSAUser {

    ArrayList<Pair<String, BigInteger[]>> pkeys = new ArrayList<>();

    Server(){
        super(512);
    }

    Server(int l){
        super(l);
    }

    public void addUser(String name, BigInteger[] user){
        //adds a user to the servers list of domains, user represented by their public key and their address
        pkeys.add(new Pair<>(name, user));
    }

    public Pair<Object[], BigInteger[]> getUserPKey(String name, BigInteger signature, BigInteger[] senderPKey){
        //user sends a request for the public key of user name and a signature
        //signature is a private key hash of the message sent
        if(verifySignature(name, signature, senderPKey)) {
            //returns a user and a signature of the username
            for (Pair<String, BigInteger[]> user : pkeys) {
                if (user.getKey().equals(name)) {
                    //System.out.println(name);
                    Pair<Object[], BigInteger[]> u;
                    Object[] signedUser = new Object[2];
                    signedUser[0] = user.getKey();
                    System.out.println(signedUser[0]);
                    signedUser[1] = signMessage(user.getKey());

                    u = new Pair<>(signedUser, user.getValue());
                    return u;
                }
            }
        }
        return null; //if user doesn't exist return null
    }



}
