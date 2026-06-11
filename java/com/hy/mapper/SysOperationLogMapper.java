package com.hy.mapper;

import com.hy.pojo.SysOperationLog;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SysOperationLogMapper {
    void insert(@Param("log") SysOperationLog log);
    List<SysOperationLog> findByPage(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("offset") Integer offset, @Param("size") Integer size);
    Long count(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    SysOperationLog findById(@Param("id") Long id);
}
