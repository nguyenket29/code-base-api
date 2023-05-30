package com.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import static com.java.constant.Constants.AES_SECRET;

public final class HashUtil {
    private static final Logger log = LoggerFactory.getLogger(HashUtil.class);
    private final static int GCM_IV_LENGTH = 12;
    private final static int GCM_TAG_LENGTH = 16;

    public static String aesEncrypt(String privateString, String secretKey) {
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "AES");
            byte[] iv = new byte[GCM_IV_LENGTH];
            (new SecureRandom()).nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);

            byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static String aesDecrypt(String encrypted, String secretKey) {
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "AES");

            byte[] decoded = Base64.getDecoder().decode(encrypted);

            byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

            byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

            return new String(ciphertext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(HashUtil.aesEncrypt("Nguyenvietket2906", AES_SECRET));
    }
}
