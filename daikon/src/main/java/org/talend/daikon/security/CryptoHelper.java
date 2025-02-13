// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.talend.daikon.crypto.EncodingUtils;
import org.talend.daikon.crypto.KeySources;

/**
 * Encrypt and decrypt strings, encapsulating the cryptography algorithm and configured by a passphrase.
 *
 * @deprecated This class uses and encourages constant passphrases usage that makes encryption <b>highly unsecure</b>.
 * Users of this class are encouraged to migrate to crypto-utils module (in Daikon).
 */
public class CryptoHelper implements Function<String, String> {

    private static final String UTF8 = "UTF8"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private Cipher ecipher;

    private Cipher dcipher;

    // Iteration count
    private int iterationCount = 29;

    public static final String PASSPHRASE;

    static {
        try {
            final byte[] key = KeySources.file("system.encryption.key").getKey();
            PASSPHRASE = new String(key, EncodingUtils.ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load default pass phrase.", e);
        }
    }

    /**
     * @param passPhrase the pass phrase used to encrypt and decrypt strings.
     */
    public CryptoHelper(String passPhrase) {
        try {
            // 8-byte Salt
            final byte[] salt = KeySources.file("system.encryption.salt").getKey();

            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec); //$NON-NLS-1$
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * @param str The plaintext string to encrypt.
     * @return The ecrypted string, or null if an error occurred.
     */
    public String encrypt(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] utf8 = str.getBytes(UTF8);
            byte[] enc = ecipher.doFinal(utf8);
            return CryptoHelper.encode64(enc);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param str The encrypted string to decrypt.
     * @return The plaintext string, or null if an error occurred.
     */
    public String decrypt(String str) {
        if (str == null) {
            return null;
        }
        // if empty, no need decrypt.
        if (EMPTY_STRING.equals(str)) {
            return EMPTY_STRING;
        }
        try {
            byte[] dec = CryptoHelper.decode64(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, UTF8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param str Binary data to encode.
     * @return The string encoded in Base64, or "" if an encoding error occurred.
     */
    private static String encode64(byte[] str) {
        try {
            return new String(Base64.encodeBase64(str), UTF8);
        } catch (UnsupportedEncodingException e) {
            return EMPTY_STRING;
        }
    }

    /**
     * @param str A Base64 string to decode.
     * @return The decoded binary data, or an empty array if a decoding error occurred.
     */
    private static byte[] decode64(String str) {
        try {
            return Base64.decodeBase64(str.getBytes(UTF8));
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    public static final CryptoHelper getDefault() {
        return new CryptoHelper(PASSPHRASE);
    }

    @Override
    public String apply(String s) {
        return encrypt(s);
    }
}
