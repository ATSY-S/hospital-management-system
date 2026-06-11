package com.hy.service;

import com.hy.mapper.DepartmentMapper;
import com.hy.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> list() {
        return departmentMapper.findAll();
    }

    public Department getById(Long id) {
        return departmentMapper.findById(id);
    }
}
