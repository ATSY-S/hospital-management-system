package com.hy.service;

import com.hy.mapper.RegisterMapper;
import com.hy.pojo.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RegisterService {
    @Autowired
    private RegisterMapper registerMapper;

    public List<Register> list(String patientName, String department, String status, LocalDate date, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return registerMapper.findByPage(patientName, department, status, date, offset, size);
    }

    public Long count(String patientName, String department, String status, LocalDate date) {
        return registerMapper.count(patientName, department, status, date);
    }

    public Register getById(Long id) {
        return registerMapper.findById(id);
    }

    public void create(Register register) {
        registerMapper.insert(register);
    }

    public void update(Register register) {
        registerMapper.update(register);
    }

    public void delete(Long id) {
        registerMapper.delete(id);
    }

    public List<Map<String, Object>> getRegisterTrend(Integer days) {
        return registerMapper.getRegisterTrend(days);
    }

    public List<Map<String, Object>> getDepartmentCount() {
        return registerMapper.getDepartmentCount();
    }

    public Integer getTodayRegisterCount() {
        Integer count = registerMapper.getTodayRegisterCount();
        return count == null ? 0 : count;
    }
    
    public Integer getYesterdayRegisterCount() {
        Integer count = registerMapper.getYesterdayRegisterCount();
        return count == null ? 0 : count;
    }
}
