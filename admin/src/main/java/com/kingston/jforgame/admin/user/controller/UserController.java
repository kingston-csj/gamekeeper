package com.kingston.jforgame.admin.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kingston.jforgame.admin.domain.Roles;
import com.kingston.jforgame.admin.domain.User;
import com.kingston.jforgame.admin.user.service.UserService;
import com.kingston.jforgame.admin.vo.SimplyReply;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/currentUserName")
	public String currentUserName() {
		return userService.getCurrentUser().getNickname();
	}

	@RequestMapping("/currentUserId")
	public Long currentUserId() {
		return userService.getCurrentUser().getId();
	}

	@RequestMapping("/isAdmin")
	public Boolean isAdmin() {
		return true;
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public List<User> getUserByNickname(String nickname) {
		return userService.getUserByNickname(nickname);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public List<Roles> getAllRole() {
		return userService.getAllRole();
	}

	@RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
	public SimplyReply updateUserEnabled(Boolean enabled, Long uid) {
		return new SimplyReply("error", "更新失败!");
	}

	@RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
	public SimplyReply deleteUserById(@PathVariable Long uid) {
		if (userService.deleteUserById(uid) == 1) {
			return new SimplyReply("success", "删除成功!");
		} else {
			return new SimplyReply("error", "删除失败!");
		}
	}

	@RequestMapping(value = "/user/role", method = RequestMethod.PUT)
	public SimplyReply updateUserRoles(Long[] rids, Long id) {
		return new SimplyReply("error", "更新失败!");
	}

}
