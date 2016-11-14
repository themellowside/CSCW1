import java.math.BigInteger;

public class RSADemonstration {

    public static void main(String[] args) {

        String myString = "Hello Bob, it's Alice here! Just messaging to let you know that my implementation is working.";

        RSAUser alice = new RSAUser(512);

        String c = alice.encrypt(myString);
        String m = alice.decrypt(c);
        System.out.println(c);
        System.out.println(m);
    }

}
