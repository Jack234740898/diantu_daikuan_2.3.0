package com.utils;


import android.util.Base64;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {

    private static final String KEY_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int KEY_SIZE = 1024;

    /**
     * 使用模和指数生成RSA公钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA /None/NoPadding】
     *
     * @param modulus        模
     * @param publicExponent 指数
     */
    public static RSAPublicKey getPublicKey(String modulus, String publicExponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(publicExponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA /None/NoPadding】
     *
     * @param modulus         模
     * @param privateExponent 指数
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String privateExponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(privateExponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String data, PublicKey publicKey) throws Exception {
        //Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = KEY_SIZE / 8;
        // 加密数据长度 <= 模长-11 128-11=117
        String[] datas = splitString(data, key_len - 11);
        StringBuffer encryptText = new StringBuffer();
        //如果明文长度大于模长-11则要分组加密

        ByteBuffer byteBuffer = ByteBuffer.allocate(datas.length * 128);
        for (String s : datas) {
            byte[] b = cipher.doFinal(s.getBytes(DEFAULT_CHARSET));
            byteBuffer.put(b);
        }
        encryptText.append(Base64.encodeToString(byteBuffer.array(), Base64.NO_WRAP));
        return encryptText.toString();

    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String data, PrivateKey privateKey) throws Exception {
        //Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = KEY_SIZE / 8;
        byte[] bytes = Base64.decode(data, Base64.NO_WRAP);
        //如果密文长度大于模长则要分组解密
        StringBuffer decryptText = new StringBuffer();
        byte[][] arrays = splitArray(bytes, key_len);
        for (byte[] arr : arrays) {
            decryptText.append(new String(cipher.doFinal(arr), DEFAULT_CHARSET));
        }
        return decryptText.toString();

    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }


    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

}

