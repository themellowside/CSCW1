/**
 * Created by tom on 09/11/2016.
 */
import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {

    public static Boolean isPrime(BigInteger candidatePrime, int iterations){
        //bigIntegers are a pain to handle but it seems like the most relevant datatype
        //candidate prime number and the number of iterations
        //returns either a composite of the number or if it's probably prime
        if(candidatePrime.compareTo( BigInteger.valueOf(4)) < 0 ) {
            //if k < 4 then we know it's prime
            return true;
        }else if(candidatePrime.mod(BigInteger.valueOf(2)) == BigInteger.ZERO){
            return false;
        }else{
            int s = 0;
            BigInteger d = candidatePrime.subtract(BigInteger.ONE);
            while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
                //a is a random integer between two and candidatePrime - 1

                d = d.divide(BigInteger.valueOf(2));
                s++;
            }


            for (int i = 0; i < iterations; i++) {
                //pick randomly in the range 2, n-2
                //x = ad mod n
                BigInteger a = randomBigInt(BigInteger.valueOf(2), candidatePrime.subtract(BigInteger.valueOf(2)));

                BigInteger x = a.modPow(d, candidatePrime);
                boolean xValid = false;
                if (x.compareTo(BigInteger.ONE) == 0 || x.compareTo(candidatePrime.subtract(BigInteger.ONE)) == 0) {
                    xValid = true;
                }
                if(!xValid) {
                    for (int r = 1; r <= s; r++) {
                        if (r == s) {
                            return false;
                        }

                        x = x.modPow(BigInteger.valueOf(2), candidatePrime);

                        if (x.compareTo(BigInteger.ONE) == 0) {
                            return false;
                        } else if (x.compareTo(candidatePrime.subtract(BigInteger.ONE)) == 0) {
                            break;
                        }


                        //if x = 1 then return composite
                        //if x = n − 1 then do next LOOP

                    }
                }
                //return composite
            }
            return true;
        }


    }

    static BigInteger randomBigInt(BigInteger startRange, BigInteger endRange){
        //returns a random big integer between the ranges stated, start inclusive end exclusive
        Random rnd = new Random();
        BigInteger res;
        do {
            res = new BigInteger(endRange.bitLength(), rnd);
        } while (res.compareTo(startRange) < 0 || res.compareTo(endRange) > 0);

        return res;


    }


}
