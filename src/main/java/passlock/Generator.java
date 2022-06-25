package passlock;
import java.util.Random;

public class Generator {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final char[] ALPHANUMERIC = (LETTERS + LETTERS.toUpperCase() + DIGITS).toCharArray();

    public static String generateString(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(LETTERS.charAt(new Random().nextInt(LETTERS.length())));
        }
        return builder.toString();
    }

    public static String generateDigits(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(DIGITS.charAt(new Random().nextInt(DIGITS.length())));
        }
        return builder.toString();
    }

    public static String generateAlphanumeric(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ALPHANUMERIC[new Random().nextInt(ALPHANUMERIC.length)]);
        }
        return builder.toString();
    }
}