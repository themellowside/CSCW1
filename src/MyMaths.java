import java.math.BigInteger;

/**
 * Created by tom on 14/11/2016.
 */
public class MyMaths {

    public static BigInteger nextPrime(BigInteger n){
        n = n.add(BigInteger.valueOf(2));
        if(n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
           n = n.add(BigInteger.ONE);
        }

        while(!MillerRabin.isPrime(n, 10)) {
            n = n.add(BigInteger.valueOf(2));
        }

        return n;
    }

}
