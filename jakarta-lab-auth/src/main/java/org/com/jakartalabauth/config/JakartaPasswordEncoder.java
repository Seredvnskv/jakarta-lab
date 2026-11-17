package org.com.jakartalabauth.config;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class JakartaPasswordEncoder implements PasswordEncoder {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS = 2048;

    private static final int SALT_BYTES = 32;

    private static final int HASH_BYTES = 32;

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(ALGORITHM, rawPassword, salt, ITERATIONS, HASH_BYTES);
        return ALGORITHM + ":" + ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        if (parts.length != 4) {
            return false;
        }
        byte[] salt = Base64.getDecoder().decode(parts[2]);
        byte[] hash = Base64.getDecoder().decode(parts[3]);
        byte[] actual = pbkdf2(parts[0], rawPassword, salt, Integer.parseInt(parts[1]), hash.length);
        return MessageDigest.isEqual(hash, actual);
    }

    private static byte[] pbkdf2(String algorithm, CharSequence password, byte[] salt, int iterations, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toString().toCharArray(), salt, iterations, bytes * 8);
            return SecretKeyFactory.getInstance(algorithm).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
