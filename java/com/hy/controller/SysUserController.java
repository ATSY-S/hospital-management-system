package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.SysUser;
import com.hy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        SysUser user = sysUserService.login(username, password);
        if (user == null) {
            return new Result("500", "用户名或密码错误", null);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", java.util.UUID.randomUUID().toString());
        data.put("user", user);
        return new Result("200", "登录成功", data);
    }

    @PostMapping("/logout")
    public Result logout() {
        return new Result("200", "退出成功", null);
    }

    @GetMapping("/info")
    public Result info() {
        SysUser user = sysUserService.getById(1L);
        return new Result("200", "查询成功", user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody SysUser user) {
        sysUserService.update(user);
        return new Result("200", "更新成功", null);
    }

    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        sysUserService.changePassword(1L, oldPassword, newPassword);
        return new Result("200", "密码修改成功", null);
    }

    @PutMapping("/avatar")
    public Result updateAvatar(@RequestBody Map<String, String> params) {
        String avatar = params.get("avatar");
        sysUserService.updateAvatar(1L, avatar);
        return new Result("200", "头像更新成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(defaultValue = "") String keyword) {
        List<SysUser> list = sysUserService.list(keyword, page, size);
        Long total = sysUserService.count(keyword);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return new Result("200", "查询成功", user);
    }

    @PostMapping
    public Result create(@RequestBody SysUser user) {
        sysUserService.create(user);
        return new Result("200", "创建成功", null);
    }

    @PutMapping("/{id}")
    public Result updateById(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        sysUserService.update(user);
        return new Result("200", "更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        sysUserService.delete(id);
        return new Result("200", "删除成功", null);
    }
}
