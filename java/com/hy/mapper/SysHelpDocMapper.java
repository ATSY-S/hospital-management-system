package com.hy.mapper;

import com.hy.pojo.SysHelpDoc;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysHelpDocMapper {
    List<SysHelpDoc> findByPage(@Param("category") String category, @Param("offset") Integer offset, @Param("size") Integer size);
    Long countByCategory(@Param("category") String category);
    SysHelpDoc findById(@Param("id") Long id);
}
