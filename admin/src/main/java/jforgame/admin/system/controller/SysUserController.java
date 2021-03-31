package jforgame.admin.system.controller;

import com.mysql.cj.util.StringUtils;
import jforgame.admin.domain.SysUser;
import jforgame.admin.http.HttpResult;
import jforgame.admin.http.PageRequest;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.system.model.RoleKinds;
import jforgame.admin.system.service.SysUserService;
import jforgame.admin.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PreAuthorize("hasAuthority('sys:user:add') AND hasAuthority('sys:user:edit')")
    @PostMapping(value = "/save")
    public HttpResult save(@RequestBody SysUser record) {
        if (RoleKinds.ADMIN.equalsIgnoreCase(record.getName())) {
            return HttpResult.error("超级管理员不允许修改!");
        }
        if (StringUtils.isNullOrEmpty(record.getPassword())) {
            return HttpResult.error("密码不允许为空!");
        }
        if (sysUserService.loadUserByUsername(record.getName()) != null) {
            return HttpResult.error("用户名已存在!");
        }
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode(record.getPassword(), salt);
        record.setSalt(salt);
        record.setPassword(password);

        return HttpResult.ok(sysUserService.save(record));
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping(value = "/delete")
    public HttpResult delete(@RequestBody List<SysUser> records) {
        for (SysUser record : records) {
            SysUser sysUser = sysUserService.findById(record.getId());
            if (sysUser != null && RoleKinds.ADMIN.equalsIgnoreCase(sysUser.getName())) {
                return HttpResult.error("超级管理员不允许删除!");
            }
        }
        return HttpResult.ok(sysUserService.delete(records));
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findByName")
    public HttpResult findByUserName(@RequestParam String name) {
        return HttpResult.ok(sysUserService.loadUserByUsername(name));
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findPermissions")
    public HttpResult findPermissions(@RequestParam String name) {
        return HttpResult.ok(sysUserService.findPermissions(name));
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findUserRoles")
    public HttpResult findUserRoles(@RequestParam Long userId) {
        return HttpResult.ok(sysUserService.findUserRoles(userId));
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @PostMapping(value = "/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
        return HttpResult.ok(sysUserService.findPage(pageRequest));
    }


    @RequestMapping("/findPermissions")
    public String findPermissions() {
        List<String> result = SecurityUtils.getAuth();
        return String.join(";", result);
    }

//    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
//    public SimplyReply deleteUserById(@PathVariable Long uid) {
//        if (userService.deleteUserById(uid) == 1) {
//            return new SimplyReply("success", "删除成功!");
//        } else {
//            return new SimplyReply("error", "删除失败!");
//        }
//    }

//    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
//    public SimplyReply updateUserRoles(@RequestParam(value = "targetUser") String targetUser,
//                                       @RequestParam(value = "newPwd") String newPwd) {
//        if (StringUtils.isEmpty(targetUser)) {
//            return SimplyReply.valueOfFail("请选择渠道");
//        }
//        String myUser = currentUserName();
//        if (channelService.queryChildChannel(myUser).size() <= 1) {
//            return SimplyReply.valueOfFail("无法修改密码，请联系父渠道");
//        }
//
//        // 超级管理员可以修改所有人的密码
//        if (!SecurityUtils.hasAuth(RoleKinds.ADMIN)) {
//            if (!channelService.queryChildChannel(myUser).contains(targetUser)) {
//                return SimplyReply.valueOfFail("更新失败");
//            }
//        }
//        channelService.updatePassword(targetUser, newPwd);
//        return SimplyReply.valueOfOk("修改成功");
//    }

}

