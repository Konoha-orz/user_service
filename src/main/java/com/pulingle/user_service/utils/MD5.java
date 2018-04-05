package com.pulingle.user_service.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @Author: Teemo
 * @Description: 加盐md5
 * @Date: Created in 13:20 2018/4/5
 */
public class MD5 {

    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999));//生成盐
        int len = sb.length();
        if (len < 8) {
            for (int i = 0; i < 8 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);//加盐加密
        char[] cs = new char[40];//将盐存储
        int p=0;
        int s=0;
        for(int i=0;i<40;i++){
            if((i%2==0)||(s>=8)){
                cs[i]=password.charAt(p);
                p++;
            }else{
                cs[i]=salt.charAt(s);
                s++;
            }
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String passwordAfterMd5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[8];
        int p=0;
        int s=0;
        for(int i=0;i<40;i++){//提取盐
            if(i%2==0||s>=8){
                cs1[p] = passwordAfterMd5.charAt(i);
                p++;
            }else{
                cs2[s] = passwordAfterMd5.charAt(i);
                s++;
            }
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

}
