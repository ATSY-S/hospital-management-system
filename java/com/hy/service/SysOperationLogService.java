package com.hy.service;

import com.hy.mapper.SysOperationLogMapper;
import com.hy.pojo.SysOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysOperationLogService {
    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    public void log(SysOperationLog log) {
        sysOperationLogMapper.insert(log);
    }

    public List<SysOperationLog> list(Long userId, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return sysOperationLogMapper.findByPage(userId, startTime, endTime, offset, size);
    }

    public Long count(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return sysOperationLogMapper.count(userId, startTime, endTime);
    }

    public SysOperationLog getById(Long id) {
        return sysOperationLogMapper.findById(id);
    }
}
