package com.xinaml.robot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public final class ASEUtil {
    //密钥 (需要前端和后端保持一致)
    private static final String KEY = "abcdefgabcdefg12";
    //算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * aes解密
     *
     * @param content 内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String content) {
        try {
            content = content.replaceAll(" ", "+"); //接受到的加密后的字符串有空格，替换成+
            byte[] bytes = StringUtils.isEmpty(content) ? null : new BASE64Decoder().decodeBuffer(content);
            return StringUtils.isEmpty(content) ? null : aesDecryptByBytes(bytes, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * aes加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) {
        try {
            return Base64.encodeBase64String(aesEncryptToBytes(content, KEY));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }


    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static void main(String[] args) {
        String str = decrypt("mo/IqplxO8E50FaqamCKEk/q2niJ6ZBhjNA1p5its nANte BVJM4ZsciXqkDNVz");
        System.out.println(str);
    }
}
