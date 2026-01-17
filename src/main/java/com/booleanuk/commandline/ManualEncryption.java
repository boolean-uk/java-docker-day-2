package com.booleanuk.commandline;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ManualEncryption {
    public static void main(String[] args) {
        String password = "password";
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode(password);
        System.out.println(encoded);

        String password2 = "password";
        String alsoEncoded = encoder.encode(password2);
        System.out.println(alsoEncoded);

        System.out.println(encoder.matches("password", encoded));
        System.out.println(encoder.matches("password", alsoEncoded));
    }
}