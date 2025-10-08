package thanhtrancoder.domain_pro_be.common.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
}
