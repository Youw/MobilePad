package com.easyapp.mobilepad;

import android.support.annotation.NonNull;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by Gasper on 17.12.2015
 */
public class Cryptography {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 24;
    private static final int ITERATIONS = 1000;


    public static byte[] encrypt(@NonNull String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        byte[] pbkdf2 = constructHash(password, salt);
        if (pbkdf2 == null) return null;
        byte[] hash = Arrays.copyOf(salt,SALT_LENGTH+pbkdf2.length);
        System.arraycopy(pbkdf2,0,hash,SALT_LENGTH,pbkdf2.length);
        return hash;
    }

    public static boolean verify(@NonNull String password, @NonNull byte[] hash) {
        if (hash.length < SALT_LENGTH+1) return false;
        byte[] salt = Arrays.copyOfRange(hash, 0, SALT_LENGTH);
        byte[] pbkdf2 = Arrays.copyOfRange(hash, SALT_LENGTH, hash.length);
        byte[] test_pbkdf2 = constructHash(password,salt);
        return slowEquals(pbkdf2, test_pbkdf2);
    }

    private static byte[] constructHash(String password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(),salt,ITERATIONS,KEY_LENGTH);
        byte[] hash = null;
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            hash = keyFactory.generateSecret(spec).getEncoded();
        } catch (Exception e){}
        return hash;
    }

    private static boolean slowEquals(byte[] arr1, byte[] arr2){
        if (arr1 == null || arr2 == null) return false;
        int diff = arr1.length ^ arr2.length;
        for (int i = 0; i<arr1.length && i<arr2.length; ++i)
            diff |= arr1[i] ^ arr2[i];
        return diff == 0;
    }

}
