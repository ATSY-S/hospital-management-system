package com.hy.mapper;

import com.hy.pojo.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysUserMapper {
    SysUser findByUsername(@Param("username") String username);
    SysUser findById(@Param("id") Long id);
    List<SysUser> findByPage(@Param("keyword") String keyword, @Param("offset") Integer offset, @Param("size") Integer size);
    Long count(@Param("keyword") String keyword);
    void insert(@Param("user") SysUser user);
    void update(@Param("user") SysUser user);
    void updatePassword(@Param("id") Long id, @Param("password") String password);
    void updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);
    void delete(@Param("id") Long id);
}
