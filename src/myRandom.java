import java.math.BigInteger;

/**
 * Created by tom on 09/11/2016.
 */
public class myRandom {
    //generates a random LargeInt prime

    /*
    Generate a random 512 bit odd number, say pp

    Test to see if pp is prime; if it is, return pp; this is expected to occur after testing about Log(p)/2∼177Log(p)/2∼177 candidates
    Otherwise set p=p+2p=p+2, goto 2
    */
    public static BigInteger randomPrime(int bitLength){
        //generate a random 512 bit integer
        BigInteger randomPrime = randomBigInt(bitLength);
        //if it's even +1
        if(randomPrime.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
            randomPrime = randomPrime.add(BigInteger.ONE);
        }
        while (!MillerRabin.isPrime(randomPrime, 40)) {
            randomPrime = randomPrime.add(BigInteger.valueOf(2));
        }

        //if it's odd
            //check it's probably prime, return if true
            //if it's not probably prime, add 2, repeat
        System.out.println (randomPrime + " " + randomPrime.bitLength());

        return randomPrime;

    }

    public static BigInteger randomBigInt(int bitLength){
        //generate a random BigInteger of n bits length

        BigInteger finalVal = BigInteger.ZERO;

        while(finalVal.bitLength() < bitLength) {
            BigInteger nextVal = BigInteger.valueOf((long) Math.floor(Math.random() * 10));
            finalVal = finalVal.multiply(BigInteger.TEN).add(nextVal);
        }
        while(finalVal.bitLength() > bitLength){
            finalVal = finalVal.divide(BigInteger.valueOf(2));
        }

        return finalVal;

    }

    public static long rand(){
        return 0;
    }
}
