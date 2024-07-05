package jforgame.admin.system.controller;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jforgame.admin.http.HttpResult;
import jforgame.admin.security.JwtAuthenticationToken;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.system.service.SysUserService;
import jforgame.admin.system.vo.LoginBean;
import jforgame.admin.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class SysLoginController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private Producer producer;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
//        response.setHeader("Cache-Control", "no-store, no-cache");
//        response.setContentType("image/jpeg");
//
//        try {
//            // 生成文字验证码
//            String text = producer.createText();
//            // 生成图片验证码
//            BufferedImage image = producer.createImage(text);
//            // 保存到验证码到 session
//            request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
////            System.out.println("设置sessionid:" + request.getSession().getId());
//            try (ServletOutputStream out = response.getOutputStream()) {
//                ImageIO.write(image, "jpg", out);
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
    }

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
        String username = loginBean.getAccount();
        String password = loginBean.getPassword();
        String captcha = loginBean.getCaptcha();

        // 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
        Object kaptcha = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        System.out.println("获取sessionid:" + request.getSession().getId());
        if (kaptcha == null) {
//            return HttpResult.error("验证码已失效");
        }
//		if(!captcha.equals(kaptcha)){
//			return HttpResult.error("验证码不正确");
//		}
        // 用户信息
        UserDetails user = userService.loadUserByUsername(username);

        // 账号不存在、密码错误
        if (user == null) {
            return HttpResult.error("账号不存在");
        }
        if (!PasswordUtils.matches("", password, user.getPassword())) {
//            return HttpResult.error("密码不正确");
        }

        // 系统登录认证
        JwtAuthenticationToken token = SecurityUtils.login(request, username, password, authenticationManager);
        return HttpResult.ok(token);
    }

}
