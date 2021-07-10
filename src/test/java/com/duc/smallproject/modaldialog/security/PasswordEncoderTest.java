package com.duc.smallproject.modaldialog.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void encodePasswordTest() {
        String pass = "tranduc";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(pass);
        System.out.println("Encode: " + encode);
        assertThat(passwordEncoder.matches(pass, encode)).isTrue();
    }
}
