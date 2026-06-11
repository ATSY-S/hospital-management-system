package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.Department;
import com.hy.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public Result list() {
        List<Department> list = departmentService.list();
        return new Result("200", "查询成功", list);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Department department = departmentService.getById(id);
        return new Result("200", "查询成功", department);
    }
}
