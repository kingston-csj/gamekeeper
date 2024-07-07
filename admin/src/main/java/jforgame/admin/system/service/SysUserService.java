package jforgame.admin.system.service;

import jforgame.admin.domain.SysMenu;
import jforgame.admin.domain.SysRole;
import jforgame.admin.domain.SysUser;
import jforgame.admin.domain.SysUserRole;
import jforgame.admin.http.ColumnFilter;
import jforgame.admin.http.PageRequest;
import jforgame.admin.http.PageResult;
import jforgame.admin.security.GrantedAuthorityImpl;
import jforgame.admin.security.JwtUserDetails;
import jforgame.admin.system.dao.SysRoleDao;
import jforgame.admin.system.dao.SysUserDao;
import jforgame.admin.system.dao.SysUserRoleDao;
import jforgame.admin.system.vo.SysUserRoleVo;
import jforgame.admin.system.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserService implements UserDetailsService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleDao sysRoleDao;

    public int save(SysUser record) {
        if (record == null) {
            return 0;
        }
        sysUserDao.save(record);
        return 1;
    }

    public int delete(SysUser record) {
        if (record == null) {
            return 0;
        }
        sysUserDao.deleteById(record.getId());
        return 1;
    }

    @Transactional
    public int delete(List<SysUser> records) {
        if (CollectionUtils.isEmpty(records)) {
            return 0;
        }
        sysUserDao.deleteAllInBatch(records);
        return 1;
    }

    public SysUser findById(Long id) {
        return sysUserDao.getById(id);
    }

    public SysUser findByName(String name) {
        return sysUserDao.findByName(name);
    }

    public PageResult findPage(PageRequest request) {
        String name = getColumnFilterValue(request, "name");
        String email = getColumnFilterValue(request, "email");
        int page = Math.abs(request.getPageNum());
        int pageSize = request.getPageSize();
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);
        Page<SysUser> searchResult = sysUserDao.findAll(pageRequest);
        // 加载用户角色信息
        List<SysUserVo> sysUserVos = new ArrayList<>();
        for (SysUser sysUser : searchResult) {
            SysUserVo vo = sysUser.simpleView();
            List<SysUserRole> userRoles = findUserRoles(sysUser.getId());
            List<SysUserRoleVo> roleVos = userRoles.stream().map(SysUserRole::simpleView).collect(Collectors.toList());
            vo.setUserRoleVos(roleVos);
            vo.setRoleNames(getRoleNames(userRoles));
            sysUserVos.add(vo);
        }
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages(searchResult.getTotalPages());
        pageResult.setContent(sysUserVos);
        return pageResult;
    }

    /**
     * 获取过滤字段的值
     *
     * @param filterName
     * @return
     */
    public String getColumnFilterValue(PageRequest pageRequest, String filterName) {
        String value = null;
        ColumnFilter columnFilter = pageRequest.getColumnFilter(filterName);
        if (columnFilter != null) {
            value = columnFilter.getValue();
        }
        return value;
    }

    private String getRoleNames(List<SysUserRole> userRoles) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<SysUserRole> iter = userRoles.iterator(); iter.hasNext(); ) {
            SysUserRole userRole = iter.next();
            SysRole sysRole = sysRoleDao.getById(userRole.getRoleId());
            sb.append(sysRole.getRemark());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public Set<String> findPermissions(String userName) {
        Set<String> perms = new HashSet<>();
        List<SysMenu> sysMenus = sysMenuService.findByUser(userName);
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getPerms() != null && !sysMenu.getPerms().isEmpty()) {
                perms.add(sysMenu.getPerms());
            }
        }
        return perms;
    }

    public List<SysUserRole> findUserRoles(Long userId) {
        return sysUserRoleDao.findByUserId(userId);
    }

    public String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    public boolean isUserNameExist(String name) {
        return sysUserDao.findByName(name) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        SysUser user =  sysUserDao.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        Set<String> permissions = findPermissions(user.getName());
        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        return new JwtUserDetails(user.getName(), user.getPassword(), user.getSalt(), grantedAuthorities);
    }

}
