package com.generation.bookly;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGeneratorTest {

    @Test
    void generateHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] passwords = {"admin123", "guest123", "mario123", "laura123"};
        for (String p : passwords) {
            System.out.println(p + " -> " + encoder.encode(p));
        }
    }
}
