package com.hy.mapper;

import com.hy.pojo.Register;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RegisterMapper {
    List<Register> findByPage(@Param("patientName") String patientName, @Param("department") String department, @Param("status") String status, @Param("date") LocalDate date, @Param("offset") Integer offset, @Param("size") Integer size);
    Long count(@Param("patientName") String patientName, @Param("department") String department, @Param("status") String status, @Param("date") LocalDate date);
    Register findById(@Param("id") Long id);
    void insert(@Param("reg") Register register);
    void update(@Param("reg") Register register);
    void delete(@Param("id") Long id);
    List<Map<String, Object>> getRegisterTrend(@Param("days") Integer days);
    List<Map<String, Object>> getDepartmentCount();
    Integer getTodayRegisterCount();
    Integer getYesterdayRegisterCount();
}
