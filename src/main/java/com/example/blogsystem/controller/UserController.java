package com.example.blogsystem.controller;

import com.example.blogsystem.common.ApplicationVariable;
import com.example.blogsystem.common.PasswordUtils;
import com.example.blogsystem.common.ResultAjax;
import com.example.blogsystem.model.Userinfo;
import com.example.blogsystem.model.vo.UserinfoVO;
import com.example.blogsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册功能
     * @param userinfo
     * @return
     */
    @RequestMapping("/reg")
    public ResultAjax reg(Userinfo userinfo) {
        // 参数校验 Spring中提供了一个util类
        System.out.println("进入reg请求");
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
            || !StringUtils.hasLength(userinfo.getPassword())) {
            System.out.println("非法参数");
            return ResultAjax.fail(-1, "非法参数");
        }
        userinfo.setPassword(PasswordUtils.encrypt(userinfo.getPassword()));
        // 请求Service 进行添加操作
        int ret = userService.reg(userinfo);
        System.out.println("成功执行");
        // 将执行结果返回给前端
        return ResultAjax.success(ret);
    }

    /**
     * 登录功能
     * @param user
     * @return ResultAjax
     */
    @RequestMapping("/login")
    public ResultAjax login(UserinfoVO user, HttpServletRequest request) {
        // 校验参数
        if(user == null || !StringUtils.hasLength(user.getUsername()) || !StringUtils.hasLength(user.getPassword())) {
            // 非法登录
            return ResultAjax.fail(-1,"参数有误");
        }
        // 查询对象
            // 查询到了对象, 校验密码
            // 查询不到用户, 返回请"输入正确的用户名密码"
        Userinfo userinfodb = userService.getUserByName(user.getUsername());
        if (userinfodb == null || userinfodb.getId() == 0) {
            // 不存在用户名
            return ResultAjax.fail(-2,"用户名或密码错误");
        }
        // 比较成功之后设置session
        if (!PasswordUtils.decrypt(user.getPassword(),userinfodb.getPassword())) {
            // 密码错误
            return ResultAjax.fail(-2,"用户名或密码错误");
        }
        HttpSession session = request.getSession();
        session.setAttribute(ApplicationVariable.SESSION_USERINFO_KEY, userinfodb);
        // 返回数据给前端

        return ResultAjax.success(1);

    }

    @RequestMapping("/logout")
    public ResultAjax logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(ApplicationVariable.SESSION_USERINFO_KEY)!=null) {
            session.removeAttribute(ApplicationVariable.SESSION_USERINFO_KEY);
        }
        return ResultAjax.success(1);
    }
}
