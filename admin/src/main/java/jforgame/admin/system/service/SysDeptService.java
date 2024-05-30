package jforgame.admin.system.service;

import jforgame.admin.domain.SysDept;
import jforgame.admin.system.dao.SysDeptDao;
import jforgame.admin.system.vo.SysDeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SysDeptService {

    @Autowired
    private SysDeptDao sysDeptDao;

    public int save(SysDept record) {
        if (record == null) {
            return 0;
        }
        sysDeptDao.save(record);
        return 1;
    }

    public void delete(SysDept record) {
        sysDeptDao.deleteById(record.getId());
    }

    public int delete(List<SysDept> records) {
        if (CollectionUtils.isEmpty(records)) {
            return 0;
        }
        sysDeptDao.deleteAllInBatch(records);
        return 1;
    }

    public List<SysDeptVo> findTree() {
        List<SysDeptVo> sysDepts = new ArrayList<>();
        List<SysDept> depts = sysDeptDao.findAll();
        for (SysDept dept : depts) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                SysDeptVo vo = dept.simpleView();
                vo.setLevel(0);
                sysDepts.add(vo);
            }
        }
        findChildren(sysDepts, depts);
        return sysDepts;
    }

    private void findChildren(List<SysDeptVo> parentVos, List<SysDept> allDepts) {
        for (SysDeptVo sysDept : parentVos) {
            List<SysDeptVo> children = new ArrayList<>();
            for (SysDept dept : allDepts) {
                if (sysDept.getId() != null && sysDept.getId().equals(dept.getParentId())) {
                    SysDeptVo vo = dept.simpleView();
                    vo.setParentName(dept.getName());
                    vo.setParentName(dept.getName());
                    vo.setLevel(vo.getLevel() + 1);
                    children.add(vo);
                }
            }
            sysDept.setChildren(children);
            findChildren(children, allDepts);
        }
    }

}
