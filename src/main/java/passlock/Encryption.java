package passlock;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String encode(String phrase) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(phrase.getBytes());
    }

    public static String decode(String phrase) {
        Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(phrase));
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static String hash(String raw) {
        try {
            return toHexString(getSHA(raw));
        } catch (Exception ex) {
            return "Error";
        }
    }
}