package com.hy.mapper;

import com.hy.pojo.SysNotification;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysNotificationMapper {
    List<SysNotification> findByUserId(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("size") Integer size);
    Long countByUserId(@Param("userId") Long userId);
    Long countUnread(@Param("userId") Long userId);
    void markRead(@Param("id") Long id);
    void markAllRead(@Param("userId") Long userId);
    void delete(@Param("id") Long id);
}
