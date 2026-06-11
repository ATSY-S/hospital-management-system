package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.SysOperationLog;
import com.hy.service.SysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
public class SysOperationLogController {
    @Autowired
    private SysOperationLogService sysOperationLogService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) Long userId,
                       @RequestParam(required = false) String startTime,
                       @RequestParam(required = false) String endTime) {
        LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
        LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;
        List<SysOperationLog> list = sysOperationLogService.list(userId, start, end, page, size);
        Long total = sysOperationLogService.count(userId, start, end);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        SysOperationLog log = sysOperationLogService.getById(id);
        return new Result("200", "查询成功", log);
    }
}
