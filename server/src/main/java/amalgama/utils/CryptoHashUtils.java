package amalgama.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHashUtils {
    public static String hash(String data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance(algorithm);
        byte[] bytes = d.digest(data.getBytes());
        BigInteger bigint = new BigInteger(1, bytes);
        return bigint.toString(16);
    }
}
