package com.hy.service;

import com.hy.mapper.PatientMapper;
import com.hy.pojo.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    @Autowired
    private PatientMapper patientMapper;

    public List<Patient> findPatList() {
        return patientMapper.findPatList();
    }

    public void insertPat(Patient patient) {
        patientMapper.insertPat(patient);
    }

    public void updatePat(Patient patient) {
        patientMapper.updatePat(patient);
    }

    public void deletePat(Integer id) {
        patientMapper.deletePat(id);
    }

    public List<Patient> findPatByNamme(String patName) {
        return patientMapper.findPatByName(patName);
    }
    
    public Integer getInHospitalCount() {
        Integer count = patientMapper.getInHospitalCount();
        return count == null ? 0 : count;
    }
    
    public Integer getYesterdayInHospitalCount() {
        Integer count = patientMapper.getYesterdayInHospitalCount();
        return count == null ? 0 : count;
    }
    
    public List<Map<String, Object>> getAgeDistribution() {
        return patientMapper.getAgeDistribution();
    }
}
