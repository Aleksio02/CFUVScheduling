package ru.cfuv.cfuvscheduling.auth.hash;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(String password) {
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        // Проверяем соответствие пароля и его хеша
        return encoder.matches(rawPassword, encodedPassword);
    }
}
