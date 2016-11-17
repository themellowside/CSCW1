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
    public void addUser(Pair<String, BigInteger[]> user){
        //adds a user to the servers list of domains, user represented by their public key and their address
        pkeys.add(user);
    }

    public Pair<String[], BigInteger[]> getUserPKey(String name){
        //returns a user and a signature of the username
        for(Pair<String, BigInteger[]> user : pkeys){
            if(user.getKey().equals(name)){
                Pair<String[], BigInteger[]> u;
                String[] signedUser = new String[2];
                signedUser[0] = user.getKey();
                signedUser[1] = signMessage(signedUser[0]).toString();

                u = new Pair<>(signedUser,user.getValue());

                return u;
            }
        }
        return null; //if user doesn't exist return null
    }


    public BigInteger[] getPKey(){
        return pubKey();
    }



}
