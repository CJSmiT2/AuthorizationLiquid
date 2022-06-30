package ua.org.smit.authorizationtlx.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

public class PasswordService {

    private static final int SALT_LENGTH = 32;
    private final Alhoritm alhoritm = new Alhoritm();

    public String getSaltedHash(String password) {
        try {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LENGTH);
            return Base64.encodeBase64String(salt) + "$" + alhoritm.hashSHA256(password, salt);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("error to hash password! " + password);
        }
    }

    public boolean isPasswordsEquals(String password, String hashedPassword) {
        try {
            String[] saltAndPass = hashedPassword.split("\\$");
            if (saltAndPass.length != 2) {
                throw new IllegalStateException(
                        "The stored password have the form 'salt$hash'");
            }
            String hashOfInput = alhoritm.hashSHA256(password, Base64.decodeBase64(saltAndPass[0]));

            String expectedHash = saltAndPass[1];

            return hashOfInput.equals(expectedHash);
        } catch (IllegalStateException ex) {
            throw new RuntimeException();
        }
    }

}
