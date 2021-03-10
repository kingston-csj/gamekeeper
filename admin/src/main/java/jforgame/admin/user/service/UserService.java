package jforgame.admin.user.service;

import java.util.ArrayList;
import java.util.List;

import jforgame.admin.channel.dao.ChannelDao;
import jforgame.admin.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jforgame.admin.user.dao.RolesDao;
import jforgame.admin.domain.Roles;
import jforgame.admin.domain.User;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
    private UserDao userDao;
	@Autowired
    private RolesDao rolesDao;
	@Autowired
    private ChannelDao channelDao;
    
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetails user = userDao.findByUserName(name);
        if (user == null) {
            user = channelDao.findOne(name);
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
    
    public String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return  ((UserDetails)principal).getUsername();
        }
        return principal.toString();
    }
}
