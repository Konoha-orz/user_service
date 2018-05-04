package com.pulingle.user_service.utils;

import javax.crypto.SecretKey;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;


import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * Created by @杨健 on 2018/4/13 17:16
 *
 * @Des: Token工具类
 */

public class TokenUtil {

    /**
     * 密钥字符串
     **/
    public static final String JWT_SECRET = "hong1mu2zhi3ruan4jian5";

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = JWT_SECRET;
        byte[] encodeKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
        return key;
    }


    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt 返回Claims
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
    /**
     * 解密jwt 返回Header
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static JwsHeader parseJWTHeader(String jwt) {
        SecretKey key = generalKey();
        JwsHeader header = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getHeader();
        return header;
    }

    /**
     *   Object转json 字符串
     *
     */
    public static String generalSubject(Object object) {
        String subject = JSON.toJSONString(object);
        return subject;
    }


}
