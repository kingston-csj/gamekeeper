package jforgame.admin.system.service;

import jforgame.admin.domain.SysDict;
import jforgame.admin.http.PageRequest;
import jforgame.admin.http.PageResult;
import jforgame.admin.system.dao.SysDictDao;
import jforgame.admin.system.vo.SysDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDictService {

    @Autowired
    private SysDictDao sysDictDao;

    public int save(SysDict record) {
        sysDictDao.save(record);
        return 1;
    }

    public int delete(SysDict record) {
        sysDictDao.deleteById(record.getId());
        return 1;
    }

    public int delete(List<SysDict> records) {
        sysDictDao.deleteAllInBatch(records);
        return 1;
    }

    public PageResult findPage(PageRequest request) {
        int page = Math.abs(request.getPageNum());
        int pageSize = request.getPageSize();
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);
        Page<SysDict> searchResult = sysDictDao.findAll(pageRequest);

        List<SysDictVo> sysUserVos = new ArrayList<>();
        for (SysDict sysDict : searchResult) {
            SysDictVo vo = sysDict.simpleView();
            sysUserVos.add(vo);
        }
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages(searchResult.getTotalPages());
        pageResult.setContent(sysUserVos);
        return pageResult;
    }

    public SysDict findById(Long id) {
        return sysDictDao.getById(id);
    }


    public List<SysDict> findByLabel(String label) {
        return new ArrayList<>();
    }
}
