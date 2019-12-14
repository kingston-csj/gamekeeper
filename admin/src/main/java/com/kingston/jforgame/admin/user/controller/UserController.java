package com.kingston.jforgame.admin.user.controller;

import java.util.List;

import com.kingston.jforgame.admin.channel.service.ChannelService;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.user.model.RoleKinds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.kingston.jforgame.admin.domain.User;
import com.kingston.jforgame.admin.user.service.UserService;
import com.kingston.jforgame.admin.utils.SimplyReply;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ChannelService channelService;

	@RequestMapping("/findPermissions")
	public String findPermissions() {
		List<String> result = SecurityUtils.getAuth();
		return String.join(";", result);
	}

	@RequestMapping("/currentUserName")
	public String currentUserName() {
		return userService.getCurrentUser();
	}

	@RequestMapping("/isAdmin")
	public Boolean isAdmin() {
		return true;
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
	public SimplyReply deleteUserById(@PathVariable Long uid) {
		if (userService.deleteUserById(uid) == 1) {
			return new SimplyReply("success", "删除成功!");
		} else {
			return new SimplyReply("error", "删除失败!");
		}
	}

	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public SimplyReply updateUserRoles(@RequestParam(value = "targetUser") String targetUser,
									   @RequestParam(value = "newPwd") String newPwd) {
		if (StringUtils.isEmpty(targetUser)) {
			return SimplyReply.valueOfFail("请选择代理");
		}
		String myUser = currentUserName();
		if (channelService.queryChildChannel(myUser).size() <= 1) {
			return SimplyReply.valueOfFail("无法修改密码，请联系父代理");
		}


		// 超级管理员可以修改所有人的密码
		if (!SecurityUtils.hasAuth(RoleKinds.ADMIN)) {
			if (!channelService.queryChildChannel(myUser).contains(targetUser)) {
				return SimplyReply.valueOfFail("更新失败");
			}
		}
		channelService.updatePassword(targetUser, newPwd);
		return SimplyReply.valueOfOk("修改成功");
	}

}
