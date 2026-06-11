package com.hy.service;

import com.hy.mapper.SysHelpDocMapper;
import com.hy.pojo.SysHelpDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysHelpDocService {
    @Autowired
    private SysHelpDocMapper sysHelpDocMapper;

    public List<SysHelpDoc> list(String category, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return sysHelpDocMapper.findByPage(category, offset, size);
    }

    public Long count(String category) {
        return sysHelpDocMapper.countByCategory(category);
    }

    public SysHelpDoc getById(Long id) {
        return sysHelpDocMapper.findById(id);
    }
}
