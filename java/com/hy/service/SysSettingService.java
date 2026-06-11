package com.hy.service;

import com.hy.mapper.SysSettingMapper;
import com.hy.pojo.SysSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysSettingService {
    @Autowired
    private SysSettingMapper sysSettingMapper;

    public List<SysSetting> listByCategory(String category) {
        return sysSettingMapper.findByCategory(category);
    }

    public SysSetting getByKey(String key) {
        return sysSettingMapper.findByKey(key);
    }

    public void update(String key, String value) {
        sysSettingMapper.updateValue(key, value);
    }
}
