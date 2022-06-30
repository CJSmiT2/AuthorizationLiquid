package ua.org.smit.authorizationtlx.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;


class Alhoritm {

    private static final int ITERATIONS = 20 * 1000;
    private static final int SALT_LENGTH = 32;
    private static final int DESIRED_KEY_LENGTH = 128;

    String hashSHA256(String passwordToHash, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException();
        }
    }

    String hashPBKDF2(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Empty passwords are not supported.");
        }
        SecretKeyFactory sf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = sf.generateSecret(
                new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DESIRED_KEY_LENGTH));
        return Base64.encodeBase64String(key.getEncoded());
    }
}
