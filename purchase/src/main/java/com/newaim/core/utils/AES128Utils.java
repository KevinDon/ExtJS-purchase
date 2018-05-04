package com.newaim.core.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法加密解密工具
 */
public class AES128Utils {

    private final static String DEFAULT_ENCODING = "utf-8";

    /**
     * 加密
     * @param sSrc
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        return encrypt(sSrc, DEFAULT_ENCODING, sKey, ivParameter);
    }


    /**
     * 加密
     * @param sSrc
     * @param encodingFormat
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        //此处使用BASE64做转码。
        return new BASE64Encoder().encode(encrypted);
    }

    /**
     * 解密
     * @param sSrc
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        return  decrypt(sSrc, DEFAULT_ENCODING, sKey, ivParameter);
    }

    /**
     * 解密
     * @param sSrc
     * @param encodingFormat
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}
