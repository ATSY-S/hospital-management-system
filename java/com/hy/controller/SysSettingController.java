package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.SysSetting;
import com.hy.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/setting")
public class SysSettingController {
    @Autowired
    private SysSettingService sysSettingService;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String category) {
        List<SysSetting> list = sysSettingService.listByCategory(category);
        return new Result("200", "查询成功", list);
    }

    @GetMapping("/{key}")
    public Result getByKey(@PathVariable String key) {
        SysSetting setting = sysSettingService.getByKey(key);
        return new Result("200", "查询成功", setting);
    }

    @PutMapping
    public Result update(@RequestBody Map<String, String> params) {
        String key = params.get("key");
        String value = params.get("value");
        sysSettingService.update(key, value);
        return new Result("200", "更新成功", null);
    }
}
