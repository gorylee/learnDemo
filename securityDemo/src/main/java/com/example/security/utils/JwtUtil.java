package com.example.security.utils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author GoryLee
 * @Date 2022/12/5 00:14
 * @Version 1.0
 */
public class JwtUtil {

    /**
     * 两个常量： 过期时间；秘钥
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    public static final String SECRET = "GorryLee";

    /**
     * 生成token字符串的方法
     *
     * @param id
     * @param userName
     * @return
     */
    public static String createToken(Long id, String userName) {
        return Jwts.builder()
                //设置分类；设置过期时间 一个当前时间，一个加上设置的过期时间常量
//                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //设置token主体信息，存储用户信息
                .claim("userId", id)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 判断token是否存在与有效
     *
     * @Param token
     */
    public static boolean checkToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return false;
        }
        try {
            //验证token
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 解析token
     */
    public static Claims parseToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
    }

    /**
     * 解析token
     */
    public static Claims parseToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
    }

    /**
     * 获取过期时间
     */
    public static Date getExpiration(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 检查是否已过期
     */
    public static Boolean checkExpiration(String token) {
        Date expiration = getExpiration(token);
        if(expiration!=null && expiration.compareTo(new Date())>0){
            return false;
        }
        return true;
    }

//    public static void main(String[] args) {
//        // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaW4tdXNlciIsImlhdCI6MTY3MDI0NjIwNCwiZXhwIjoxNjcwMzMyNjA0LCJpZCI6MywidXNlck5hbWUiOiJsZ3kifQ.rjZdATwWt_rpEGkRKb_aNcWcoa2bwpxDtHr1CWgpvN8
//        String token = createToken(3L, "lgy");
////        Claims claims = parseToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaW4tdXNlciIsImlhdCI6MTY3MDI0NjIwNCwiZXhwIjoxNjcwMzMyNjA0LCJpZCI6MywidXNlck5hbWUiOiJsZ3kifQ.rjZdATwWt_rpEGkRKb_aNcWcoa2bwpxDtHr1CWgpvN8");
//        Claims claims = parseToken(token);
//        Long id = claims.get("userId",Long.class);
//        String userName = claims.get("userName",String.class);
//        System.out.println(checkExpiration(token));
//    }
}
