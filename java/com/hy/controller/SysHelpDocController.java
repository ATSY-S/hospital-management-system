package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.SysHelpDoc;
import com.hy.service.SysHelpDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/help")
public class SysHelpDocController {
    @Autowired
    private SysHelpDocService sysHelpDocService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String category,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        List<SysHelpDoc> list = sysHelpDocService.list(category, page, size);
        Long total = sysHelpDocService.count(category);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        SysHelpDoc doc = sysHelpDocService.getById(id);
        return new Result("200", "查询成功", doc);
    }
}
