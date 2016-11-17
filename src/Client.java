import java.math.BigInteger;

/**
 * Created by tom on 17/11/2016.
 */
public class Client extends RSAUser{
    String name;
    //class to wrap the demonstration and make it a little easier
    Client(String name){
        super(512);
        this.name = name;

    }

    Client(String name, int l){
        super(l);
        this.name = name;

    }





}
