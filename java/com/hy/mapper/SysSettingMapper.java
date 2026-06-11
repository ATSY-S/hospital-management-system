package com.hy.mapper;

import com.hy.pojo.SysSetting;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysSettingMapper {
    List<SysSetting> findByCategory(@Param("category") String category);
    SysSetting findByKey(@Param("keyName") String keyName);
    void updateValue(@Param("key") String key, @Param("value") String value);
}
