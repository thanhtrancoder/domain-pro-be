package thanhtrancoder.domain_pro_be.common.utils;

import java.security.SecureRandom;
import java.text.Normalizer;

public class StringUtils {
    public static String removeAccent(String input) {
        if (input == null) return null;
        // tách base character + dấu phụ
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // xoá các ký tự thuộc category Mark (dấu)
        String withoutAccent = normalized.replaceAll("\\p{M}", "");
        // nếu dùng tiếng Việt, cần xử lý ký tự đ/Đ
        withoutAccent = withoutAccent.replace('đ', 'd').replace('Đ', 'D');
        return withoutAccent;
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
}
