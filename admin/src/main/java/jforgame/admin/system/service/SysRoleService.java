package jforgame.admin.system.service;

import jforgame.admin.domain.SysMenu;
import jforgame.admin.domain.SysRole;
import jforgame.admin.domain.SysRoleMenu;
import jforgame.admin.http.PageRequest;
import jforgame.admin.http.PageResult;
import jforgame.admin.system.dao.SysMenuDao;
import jforgame.admin.system.dao.SysRoleDao;
import jforgame.admin.system.dao.SysRoleMenuDao;
import jforgame.admin.system.model.RoleKinds;
import jforgame.admin.system.vo.SysRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuDao sysMenuDao;

    public int save(SysRole record) {
        sysRoleDao.save(record);
        return 1;
    }

    public int delete(SysRole record) {
        sysRoleDao.delete(record);
        return 1;
    }

    public int delete(List<SysRole> records) {
        for (SysRole record : records) {
            delete(record);
        }
        return 1;
    }

    public SysRole findById(Long id) {
        return sysRoleDao.getById(id);
    }

    public PageResult findPage(PageRequest request) {
        int page = Math.abs(request.getPageNum());
        int pageSize = request.getPageSize();
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);
        Page<SysRole> searchResult = sysRoleDao.findAll(pageRequest);

        List<SysRoleVo> sysUserVos = new ArrayList<>();
        for (SysRole sysUser : searchResult) {
            SysRoleVo vo = sysUser.simpleView();
            sysUserVos.add(vo);
        }
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages(searchResult.getTotalPages());
        pageResult.setContent(sysUserVos);
        return pageResult;
    }

    public List<SysRole> findAll() {
        return sysRoleDao.findAll();
    }

    public List<SysMenu> findRoleMenus(Long roleId) {
        SysRole sysRole = sysRoleDao.getById(roleId);
        if (RoleKinds.ADMIN.equalsIgnoreCase(sysRole.getName())) {
            // 如果是超级管理员，返回全部
            return sysMenuDao.findAll();
        }
        return sysMenuDao.findByRoleId(roleId);
    }

    @Transactional()
    public int saveRoleMenus(List<SysRoleMenu> records) {
        if (CollectionUtils.isEmpty(records)) {
            return 1;
        }
        Long roleId = records.get(0).getRoleId();
        sysRoleMenuDao.deleteByRoleId(roleId);
        for (SysRoleMenu record : records) {
            sysRoleMenuDao.save(record);
        }
        return 1;
    }

    public List<SysRole> findByName(String name) {
        return new ArrayList<>();
    }

}