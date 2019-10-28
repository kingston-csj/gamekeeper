package com.kingston.jforgame.admin.user.controller;

import java.util.List;

import com.kingston.jforgame.admin.channel.service.ChannelService;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.user.model.RoleKInds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.kingston.jforgame.admin.domain.Roles;
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

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public List<Roles> getAllRole() {
		return userService.getAllRole();
	}

	@RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
	public SimplyReply deleteUserById(@PathVariable Long uid) {
		if (userService.deleteUserById(uid) == 1) {
			return new SimplyReply("success", "删除成功!");
		} else {
			return new SimplyReply("error", "删除失败!");
		}
	}

	@RequestMapping(value = "/resetPwd", method = RequestMethod.PUT)
	public SimplyReply updateUserRoles(@RequestParam(value = "targetUser") String targetUser,
									   @RequestParam(value = "oldPwd") String oldPwd,
									   @RequestParam(value = "newPwd") String newPwd) {
		String myUser = currentUserName();
		// 超级管理员可以修改所有人的密码
		if (!SecurityUtils.hasAuth(RoleKInds.ADMIN)) {
			if (!channelService.queryChildChannel(myUser).contains(targetUser)) {
				return SimplyReply.valueOfFail("更新失败");
			}
		}
		UserDetails userDetails = userService.loadUserByUsername(myUser);
		// 修改自己的密码，需要验证旧密码
		if (myUser.equals(targetUser)) {
			if (!userDetails.getPassword().equals(oldPwd)) {
				return SimplyReply.valueOfFail("原密码错误");
			}
		}

		channelService.updatePassword(targetUser, newPwd);
		return SimplyReply.valueOfOk("修改成功");
	}

}
