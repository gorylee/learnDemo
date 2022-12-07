package com.example.security.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import example.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;


@Component
@Slf4j
public class WebUtil {

    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";

    private static final WebUtil webUtils = new WebUtil();


    /**
     * 获取请求对象
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    /**
     * 获取web根目录
     *
     * @return web根目录
     */
    public static String getRootPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }
        return path.getAbsolutePath();
    }


    /**
     * 获取请求头中的token
     */
    public static String getToken(){
        String token = "";
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(requestAttributes)) return token;
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        //先从请求头获取
        token = request.getHeader("token");
        if(StrUtil.isEmpty(token)){
            //从cookies中获取
            Cookie[] cookies = request.getCookies();
            if(cookies!=null&&cookies.length>0){
                int length = cookies.length;
                for (int i = 0; i < length; i++) {
                    Cookie cookie = cookies[i];
                    if("token".equalsIgnoreCase(cookie.getName())){
                        token = cookie.getValue();
                        return token;
                    }
                }
            }
            //从url中获取
            if(StrUtil.isEmpty(token)){
                token = request.getParameter("token");
                if(!StrUtil.isEmpty(token)) return token;
            }
//            throw new ResultException(ResultEnum.NO_LOGIN.getKey(),"请先登录");
        }
        return token;
    }

    /**
     * 获取用户名
     */
    public static String getName(){
        String token = WebUtil.getToken();
        if(StrUtil.isBlank(token)){
            return "";
        }
        Claims claims = JwtUtil.parseToken(token);
        Object name = claims.get("userName");
        if(Objects.isNull(name)) return "";
        return (String)name;
    }

    /**
     * 获取用户名
     */
    public static String getUsername(){
        Claims claims = JwtUtil.parseToken(WebUtil.getToken());
        Object username = claims.get("userName");
        if(Objects.isNull(username)) return "";
        return (String)username;
    }

    /**
     * 是否登录
     */
    public static boolean isNonLogin(){
        String token = WebUtil.getToken();
        if(StrUtil.isBlank(token)) return false;
        return JwtUtil.checkExpiration(token);
//        return JwtUtils.isExpired(token)&&!RedisUtils.hasKey(token);
    }

    /**
     * 获取当前登录用户信息 （如果token存储用户信息）
     */
//    public static <T> T getCurrentUser(Class<T> clazz){
//        Object object = RedisUtils.get(getToken());
//        if(Objects.isNull(object)){
//            throw new ResultException(ResultEnum.NO_LOGIN.getKey(),"请重新登录");
//        }
//        try{
//            T t = clazz.cast(object);
//            return t;
//        }catch (Exception e){
//            throw new ResultException(ResultEnum.NO_LOGIN.getKey(),"请重新登录");
//        }
//    }


    /**
     * 获取token中的id (可以存会员id)
     */
    public static Long getId(){
        String token = WebUtil.getToken();
        if(StrUtil.isBlank(token)){
            return 0L;
        }
        Claims claims = JwtUtil.parseToken(token);
        return Long.valueOf(claims.get("userId").toString()) ;
    }

    /**
     * 获取token中的id (可以存会员id)
     */
    public static Long getId(String token){
        if(StrUtil.isBlank(token)){
            return 0L;
        }
        Claims claims = JwtUtil.parseToken(token);
//        return Long.valueOf(claims.getId());
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 判断用户是否有操作权限
     *
     * 备注： 这里待优化，应该拿角色去判断，提高效率，减少内存存储空间的占用
     **/
//    public static boolean hasPermission(String permission){
//        if(StrUtil.isBlank(permission)) return false;
////        permission ="["+permission.replaceAll("\\|\\|", "]#[")+"]";
//        permission =permission.replaceAll("\\|\\|", "#");
//        String key = RedisKeyConstants.USER_RESOURCE_PRE+WebUtils.getId();
//        String[] permissions = permission.split("#");
//        List<Object> cacheList = RedisUtils.getList(key);
//        if(CollUtil.isEmpty(cacheList)) return false;
//        JSONArray arrList =(JSONArray)cacheList.get(0);
//        List<String> cacheStrs = arrList.toJavaList(String.class);
//        for (String s : permissions) {
//            for (String o : cacheStrs) {
//                if(o.equals(s.trim())){
//                    return true;
//                }
//            }
//        }
//        return false;
////        return webUtils.resourceService.hasPermission(permission);
//    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCALHOST.equals(ipAddress)) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error(e.getMessage());
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(SEPARATOR) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 链接编码
     *
     * @param url 链接
     * @return 编码后的链接
     */
    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("链接编码失败",e);
        }
        return "";
    }

    /**
     * 获取请求路径
     *
     * @return 请求路径
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return request.getRequestURL().toString();
    }

    /**
     * 认证授权异常处理
     * @param response
     * @param data
     * @return
     */
    public static String renderString(HttpServletResponse response, String data){
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
