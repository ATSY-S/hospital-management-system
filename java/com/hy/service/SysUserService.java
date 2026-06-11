package com.hy.service;

import com.hy.mapper.SysUserMapper;
import com.hy.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUser login(String username, String password) {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || !user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return null;
        }
        if (user.getStatus() == 0) {
            return null;
        }
        return user;
    }

    public SysUser getById(Long id) {
        return sysUserMapper.findById(id);
    }

    public List<SysUser> list(String keyword, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return sysUserMapper.findByPage(keyword, offset, size);
    }

    public Long count(String keyword) {
        return sysUserMapper.count(keyword);
    }

    public void create(SysUser user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        sysUserMapper.insert(user);
    }

    public void update(SysUser user) {
        sysUserMapper.update(user);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.findById(id);
        if (user != null && user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
            sysUserMapper.updatePassword(id, DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        }
    }

    public void updateAvatar(Long id, String avatar) {
        sysUserMapper.updateAvatar(id, avatar);
    }

    public void delete(Long id) {
        sysUserMapper.delete(id);
    }
}
