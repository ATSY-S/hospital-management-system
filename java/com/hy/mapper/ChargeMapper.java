package com.hy.mapper;

import com.hy.pojo.Charge;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ChargeMapper {
    List<Charge> findByPage(@Param("patientName") String patientName, @Param("chargeType") String chargeType, @Param("status") String status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("offset") Integer offset, @Param("size") Integer size);
    Long count(@Param("patientName") String patientName, @Param("chargeType") String chargeType, @Param("status") String status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    Charge findById(@Param("id") Long id);
    void insert(@Param("charge") Charge charge);
    void pay(@Param("id") Long id, @Param("paymentMethod") String paymentMethod);
    void delete(@Param("id") Long id);
    BigDecimal getTodayRevenue();
    Integer getTodayPrescriptionCount();
    BigDecimal getYesterdayRevenue();
    Integer getYesterdayPrescriptionCount();
}
