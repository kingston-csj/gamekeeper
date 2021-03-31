package jforgame.admin.system.service;

import jforgame.admin.domain.SysMenu;
import jforgame.admin.domain.SysRole;
import jforgame.admin.http.PageRequest;
import jforgame.admin.http.PageResult;
import jforgame.admin.system.dao.SysMenuDao;
import jforgame.admin.system.vo.SysMenuVo;
import jforgame.admin.system.vo.SysRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuDao sysMenuDao;

    public int save(SysMenu record) {
        if (record.getParentId() == null) {
            record.setParentId(0L);
        }
        sysMenuDao.save(record);
        return 1;
    }

    public int delete(SysMenu record) {
        sysMenuDao.deleteById(record.getId());
        return 1;
    }

    public int delete(List<SysMenu> records) {
        for (SysMenu record : records) {
            delete(record);
        }
        return 1;
    }

    public SysMenu findById(Long id) {
        return sysMenuDao.getOne(id);
    }

    public PageResult findPage(PageRequest request) {
        int page = Math.abs(request.getPageNum());
        int pageSize = request.getPageSize();
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);
        Page<SysMenu> searchResult = sysMenuDao.findAll(pageRequest);

        List<SysMenuVo> sysUserVos = new ArrayList<>();
        for (SysMenu sysUser : searchResult) {
            SysMenuVo vo = sysUser.simpleView();
            sysUserVos.add(vo);
        }
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages(searchResult.getTotalPages());
        pageResult.setContent(sysUserVos);
        return pageResult;
    }

    public List<SysMenuVo> findTree(String userName, int menuType) {
        List<SysMenuVo> result = new ArrayList<>();
        List<SysMenu> menus = findByUser(userName);
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                SysMenuVo vo = menu.simpleView();
                vo.setLevel(0);
                if (!exists(result, menu)) {
                    result.add(vo);
                }
            }
        }
        result.sort(Comparator.comparing(SysMenuVo::getOrderNum));
        findChildren(result, menus, menuType);
        return result;
    }

    public List<SysMenu> findByUser(String userName) {
        if (userName == null || "".equals(userName) || "admin".equalsIgnoreCase(userName)) {
            return sysMenuDao.findAll();
        }
        return sysMenuDao.findByUserName(userName);
    }

    private void findChildren(List<SysMenuVo> parentVos, List<SysMenu> allMenus, int menuType) {
        for (SysMenuVo menuVo : parentVos) {
            List<SysMenuVo> children = new ArrayList<>();
            for (SysMenu menu : allMenus) {
                if (menuType == 1 && menu.getType() == 2) {
                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
                    continue;
                }
                if (menuVo.getId() != null && menuVo.getId().equals(menu.getParentId())) {
                    if (!exists(children, menu)) {
                        SysMenuVo vo = menu.simpleView();
                        vo.setParentName(menuVo.getName());
                        vo.setLevel(menuVo.getLevel() + 1);
                        children.add(vo);
                    }
                }
            }
            menuVo.setChildren(children);
            children.sort(Comparator.comparing(SysMenuVo::getOrderNum));
            findChildren(children, allMenus, menuType);
        }
    }

    private boolean exists(List<SysMenuVo> sysMenus, SysMenu sysMenu) {
        boolean exist = false;
        for (SysMenuVo menu : sysMenus) {
            if (menu.getId().equals(sysMenu.getId())) {
                exist = true;
            }
        }
        return exist;
    }

}