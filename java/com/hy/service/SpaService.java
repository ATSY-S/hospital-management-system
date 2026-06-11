package com.hy.service;

import com.hy.mapper.SpaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class SpaService {

    @Autowired
    private SpaMapper spaMapper;

    public Integer getHosCount() {
        return spaMapper.getHosCount();
    }

    public Integer getPatCount(){
        return spaMapper.getPatCount();
    }

    public List<Map<String, Integer>> getHosPatCount() {
        return spaMapper.getHosPatCount();
    }

    public Integer getSJHosCount() {
        return spaMapper.getSJHosCount();
    }

    public List<Map<String, Integer>> getPatAge() {
        return spaMapper.getPatAge();
    }

    public Integer getPatSex() {
        return spaMapper.getPatSex();
    }
}
