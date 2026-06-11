package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.Medicine;
import com.hy.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String code) {
        List<Medicine> list = medicineService.list(name, code, page, size);
        Long total = medicineService.count(name, code);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Medicine medicine = medicineService.getById(id);
        return new Result("200", "查询成功", medicine);
    }

    @PostMapping
    public Result create(@RequestBody Medicine medicine) {
        medicineService.create(medicine);
        return new Result("200", "创建成功", null);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Medicine medicine) {
        medicine.setId(id);
        medicineService.update(medicine);
        return new Result("200", "更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        medicineService.delete(id);
        return new Result("200", "删除成功", null);
    }
}
