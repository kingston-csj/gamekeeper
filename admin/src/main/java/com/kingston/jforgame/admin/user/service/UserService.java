package com.kingston.jforgame.admin.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingston.jforgame.admin.user.dao.RolesDao;
import com.kingston.jforgame.admin.user.dao.UserDao;
import com.kingston.jforgame.admin.domain.Roles;
import com.kingston.jforgame.admin.domain.User;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
    private UserDao userDao;
	@Autowired
    private RolesDao rolesDao;
    
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findByUserName(name);
        if (user == null) {
            return new User();
        }
        return user;
    }

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(User user) {
    	return 2;
    }


    public List<User> getUserByNickname(String nickname) {
    	User user = userDao.findByUserName(nickname);
    	
    	List<User> result = new ArrayList<>();
    	result.add(user);
        return result;
    }

    public List<Roles> getAllRole() {
        return rolesDao.findAll();
    }


    public int deleteUserById(Long uid) {
        userDao.delete(uid);
        
        return 1;
    }


    public User getUserById(Long id) {
        return userDao.findById(id);
    }
    
    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
