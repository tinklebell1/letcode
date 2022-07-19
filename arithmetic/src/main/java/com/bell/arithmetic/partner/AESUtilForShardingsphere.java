package com.bell.arithmetic.partner;


import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtilForShardingsphere {

    private static final String AES = "AES";
    private static final String AES_KEY = "rLzQ3G";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//算法/工作模式/填充方式

    private static SecretKey reservedSecretKey = null;

    static {
        reservedSecretKey = getKey();
    }

    public static String encrypt(String bef_aes) {
        if (!StringUtils.hasText(bef_aes)) {
            return bef_aes;
        }
        byte[] byteContent = null;
        try {
            byteContent = bef_aes.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = reservedSecretKey == null ? getKey() : reservedSecretKey;
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("finally")
    public static String decrypt(String aft_aes) {
        if (!StringUtils.hasText(aft_aes)) {
            return aft_aes;
        }
        String bef_aes = null;
        try {
            byte[] content = parseHexStr2Byte(aft_aes);
            SecretKey secretKey = reservedSecretKey == null ? getKey() : reservedSecretKey;
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            assert content != null;
            byte[] result = cipher.doFinal(content);
            bef_aes = new String(result, StandardCharsets.UTF_8);
            // @2020-11-11 去掉乱码判定
//            if (isMessyCode(bef_aes)) {
//                bef_aes = null;
//            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            return bef_aes;
        }

    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int value = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
            result[i] = (byte) value;
        }
        return result;
    }

    private static SecretKey getKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(AESUtilForShardingsphere.AES_KEY.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("初始化密钥出现异常");
        }
    }



}
