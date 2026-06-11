package com.hy.service;

import com.hy.mapper.SysNotificationMapper;
import com.hy.pojo.SysNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysNotificationService {
    @Autowired
    private SysNotificationMapper sysNotificationMapper;

    public List<SysNotification> list(Long userId, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return sysNotificationMapper.findByUserId(userId, offset, size);
    }

    public Long count(Long userId) {
        return sysNotificationMapper.countByUserId(userId);
    }

    public Long countUnread(Long userId) {
        return sysNotificationMapper.countUnread(userId);
    }

    public void markRead(Long id) {
        sysNotificationMapper.markRead(id);
    }

    public void markAllRead(Long userId) {
        sysNotificationMapper.markAllRead(userId);
    }

    public void delete(Long id) {
        sysNotificationMapper.delete(id);
    }
}
