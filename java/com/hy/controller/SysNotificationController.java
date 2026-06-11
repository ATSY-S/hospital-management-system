package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.SysNotification;
import com.hy.service.SysNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class SysNotificationController {
    @Autowired
    private SysNotificationService sysNotificationService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        List<SysNotification> list = sysNotificationService.list(1L, page, size);
        Long total = sysNotificationService.count(1L);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/unreadCount")
    public Result unreadCount() {
        Long count = sysNotificationService.countUnread(1L);
        return new Result("200", "查询成功", count);
    }

    @PutMapping("/read/{id}")
    public Result markRead(@PathVariable Long id) {
        sysNotificationService.markRead(id);
        return new Result("200", "标记已读成功", null);
    }

    @PutMapping("/readAll")
    public Result markAllRead() {
        sysNotificationService.markAllRead(1L);
        return new Result("200", "全部已读成功", null);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        sysNotificationService.delete(id);
        return new Result("200", "删除成功", null);
    }
}
