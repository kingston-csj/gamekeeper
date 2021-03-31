//package jforgame.admin.security;
//
//import jforgame.admin.domain.SysUser;
//import jforgame.admin.service.SysUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Override`
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUser user = sysUserService.findByName(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("该用户不存在");
//        }
//        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
//        Set<String> permissions = sysUserService.findPermissions(user.getName());
//        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
//        return new JwtUserDetails(user.getName(), user.getPassword(), user.getSalt(), grantedAuthorities);
//    }
//}