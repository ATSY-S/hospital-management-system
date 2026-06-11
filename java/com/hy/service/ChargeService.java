package com.hy.service;

import com.hy.mapper.ChargeMapper;
import com.hy.pojo.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChargeService {
    @Autowired
    private ChargeMapper chargeMapper;

    public List<Charge> list(String patientName, String chargeType, String status, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return chargeMapper.findByPage(patientName, chargeType, status, startTime, endTime, offset, size);
    }

    public Long count(String patientName, String chargeType, String status, LocalDateTime startTime, LocalDateTime endTime) {
        return chargeMapper.count(patientName, chargeType, status, startTime, endTime);
    }

    public Charge getById(Long id) {
        return chargeMapper.findById(id);
    }

    public void create(Charge charge) {
        chargeMapper.insert(charge);
    }

    public void pay(Long id, String paymentMethod) {
        chargeMapper.pay(id, paymentMethod);
    }

    public void delete(Long id) {
        chargeMapper.delete(id);
    }

    public BigDecimal getTodayRevenue() {
        BigDecimal revenue = chargeMapper.getTodayRevenue();
        return revenue == null ? BigDecimal.ZERO : revenue;
    }

    public Integer getTodayPrescriptionCount() {
        Integer count = chargeMapper.getTodayPrescriptionCount();
        return count == null ? 0 : count;
    }
    
    public BigDecimal getYesterdayRevenue() {
        BigDecimal revenue = chargeMapper.getYesterdayRevenue();
        return revenue == null ? BigDecimal.ZERO : revenue;
    }
    
    public Integer getYesterdayPrescriptionCount() {
        Integer count = chargeMapper.getYesterdayPrescriptionCount();
        return count == null ? 0 : count;
    }
}
