package com.example.blogsystem.common;

import com.example.blogsystem.model.Userinfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * session工具
 */
public class SessionUtils {
    public static Userinfo getUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(ApplicationVariable.SESSION_USERINFO_KEY) != null) {
            // 登录状态
            return (Userinfo) session.getAttribute(ApplicationVariable.SESSION_USERINFO_KEY);
        }

        return null;
    }
}
