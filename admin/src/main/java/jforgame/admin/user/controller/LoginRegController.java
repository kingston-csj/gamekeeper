package jforgame.admin.user.controller;

import jforgame.admin.security.JwtAuthenticatioToken;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jforgame.admin.domain.User;
import jforgame.admin.utils.SimplyReply;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

    @RequestMapping("/login_error")
    public SimplyReply loginError() {
        return new SimplyReply("error", "登录失败!");
    }

    @RequestMapping("/login_success")
    public SimplyReply loginSuccess() {
        return new SimplyReply("success", "登录成功!");
    }

    @RequestMapping("/login_page")
    public SimplyReply loginPage() {
        return new SimplyReply("error", "尚未登录，请登录!");
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public SimplyReply login(@RequestParam(value = "userName", defaultValue = "1") String userName,
                             @RequestParam(value = "password", defaultValue = "1") String password, HttpServletRequest request) throws IOException {
        // 用户信息
        UserDetails user = userService.loadUserByUsername(userName);
        // 账号不存在、密码错误
        if (user == null || !password.equals(user.getPassword())) {
            return new SimplyReply("error", "尚未登录，请登录!");
        }

        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, userName, user.getPassword(), authenticationManager);

        return new SimplyReply("success", token.getToken());
    }
    @RequestMapping("/reg")
    public SimplyReply reg(User user) {
        int result = userService.reg(user);
        if (result == 0) {
            //成功
            return new SimplyReply("success", "注册成功!");
        } else if (result == 1) {
            return new SimplyReply("error", "用户名重复，注册失败!");
        } else {
            //失败
            return new SimplyReply("error", "注册失败!");
        }
    }
}
