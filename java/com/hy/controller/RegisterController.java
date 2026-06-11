package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.Register;
import com.hy.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String patientName,
                       @RequestParam(required = false) String department,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String date) {
        LocalDate registerDate = date != null ? LocalDate.parse(date) : null;
        List<Register> list = registerService.list(patientName, department, status, registerDate, page, size);
        Long total = registerService.count(patientName, department, status, registerDate);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Register register = registerService.getById(id);
        return new Result("200", "查询成功", register);
    }

    @PostMapping
    public Result create(@RequestBody Register register) {
        registerService.create(register);
        return new Result("200", "创建成功", null);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Register register) {
        register.setId(id);
        registerService.update(register);
        return new Result("200", "更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        registerService.delete(id);
        return new Result("200", "删除成功", null);
    }
}
