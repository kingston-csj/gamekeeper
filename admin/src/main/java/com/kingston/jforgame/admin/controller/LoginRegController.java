package com.kingston.jforgame.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingston.jforgame.admin.domain.User;
import com.kingston.jforgame.admin.service.UserService;
import com.kingston.jforgame.admin.vo.SimplyReply;

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

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    public SimplyReply loginPage() {
        return new SimplyReply("error", "尚未登录，请登录!");
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
