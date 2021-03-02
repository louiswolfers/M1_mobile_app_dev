package com.chrisney.enigma;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EnigmaUtils {
   private final static int[] data = {55, 65, 80, 111, 77, 89, 120, 73, 52, 114, 85, 97, 107, 89, 106, 53, 122, 83, 98, 73, 50, 121, 99, 118, 35, 76, 68, 67, 67, 111, 56, 74};
   public static String enigmatization(byte[] enc) {
        try {
            byte[] keyValue  = keyToBytes(data);
            byte[] result = decrypt(keyValue, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static byte[] decrypt(byte[] keyValue, byte[] encrypted)
            throws Exception {
        SecretKey skeySpec = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParams);
        return cipher.doFinal(encrypted);
    }
    private static byte[] keyToBytes(int[] key) {
        int size = 16 * (key.length / 16);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append((char) key[i]);
        }
        return builder.toString().getBytes();
    }
}
