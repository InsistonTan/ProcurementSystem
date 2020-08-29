package com.huiduoduo.ProcurementSystem.utils;

import org.apache.ibatis.annotations.Insert;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author TanJifeng
 * @Description 加密、验证工具类
 * @date 2020/8/17 20:31
 */
public class EncryptionUtil {
    public static String encryptText(String plainText){
        String cipherText= DigestUtils.md5DigestAsHex(plainText.getBytes());
        return cipherText;
    }
    public static boolean verify(String plainText,String cipherText){
        if(cipherText.equals(encryptText(plainText)))
            return true;
        else
            return false;
    }
    public static void main(String[] args){
        String a="admin";
        String encrypt="21232f297a57a5a743894a0e4a801fc3";
    }
}
