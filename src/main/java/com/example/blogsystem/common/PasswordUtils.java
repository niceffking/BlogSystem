package com.example.blogsystem.common;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 密码工具类
 */
public class PasswordUtils {
    /**
     * 加盐加密
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        // 盐值
        String salt = UUID.randomUUID().toString().replaceAll("-","");
        // 将密码 + 盐值 得到加密
        String finalPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));
        // 返回

        return salt + '$' + finalPassword;
    }

    /**
     * 加盐解密
     *
     * @param password   待验证的密码
     * @param dbPassword 数据库中的密码 盐值 + $ + 盐值密码
     */
    public static boolean decrypt(String password, String dbPassword) {
        if (!StringUtils.hasLength(password) || !StringUtils.hasLength(dbPassword) || dbPassword.length() != 65) {
            return false;
        }
        System.out.println(password + "\n");
        System.out.println(dbPassword);
        // 得到盐值
        String[] valueArr = dbPassword.split("\\$");
        if (valueArr.length != 2) {
            System.out.println("长度不符合标准");
            return true;
        }
        String salt = valueArr[0];
        String saltPassword = valueArr[1];

        String finalPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));
        System.out.println("传进来的password进行加盐之后的：\n" + finalPassword);
        if (finalPassword.equals(saltPassword)) {
            return true;
        }
        return false;
    }
}
