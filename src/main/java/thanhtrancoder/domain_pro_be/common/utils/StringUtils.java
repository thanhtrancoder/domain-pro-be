package thanhtrancoder.domain_pro_be.common.utils;

import java.security.SecureRandom;
import java.text.Normalizer;

public class StringUtils {
    public static String removeAccent(String input) {
        if (input == null) return null;
        // split into base character + diacritics
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // remove characters in the Mark category (diacritics)
        String withoutAccent = normalized.replaceAll("\\p{M}", "");
        // handle Vietnamese-specific characters (map diacritic 'd' variants to ASCII d/D)
        withoutAccent = withoutAccent.replace('\u0111', 'd').replace('\u0110', 'D');
        return withoutAccent;
    }

    public static String removeAllWhitespace(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", "");
    }

    public static String removeSpecialCharacters(String input) {
        if (input == null) {
            return null;
        }
        // Keep letters, digits, hyphen and dot; remove other special characters
        return input.replaceAll("[^a-zA-Z0-9\\-\\.]", "");
    }


    public static String generateNumericCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);  // 0-9
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String getEmailUsername(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(0, email.indexOf("@"));
    }
}

