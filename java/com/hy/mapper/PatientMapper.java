package com.hy.mapper;

import com.hy.pojo.Patient;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PatientMapper {

    List<Patient> findPatList();

    void insertPat(@Param("pat") Patient patient);

    void updatePat(@Param("pat")Patient patient);

    void deletePat(@Param("id") Integer id);

    List<Patient> findPatByName(@Param("patName") String patName);
    
    Integer getInHospitalCount();
    
    Integer getYesterdayInHospitalCount();
    
    List<Map<String, Object>> getAgeDistribution();
}
