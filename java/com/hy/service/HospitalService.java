package com.hy.service;


import com.hy.controller.HospitalController;
import com.hy.mapper.HospitalMapper;
import com.hy.pojo.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

        @Autowired
        private HospitalMapper hospitalMapper;

        public List<Hospital> findHosList(){
            return hospitalMapper.findHosList();
        }

    public Hospital selectHosByName(String hospitalName) {
            return  hospitalMapper.selectHosByName(hospitalName);
    }

    public void insertHos(Hospital hospital) {
            hospitalMapper.insertHos(hospital);
    }

    public void deleteById(Integer id) {
            hospitalMapper.deleteById(id);

    }

    public void deleteByIds(List<Integer> ids) {
            hospitalMapper.deleteByIds(ids);
    }

    public void updateHios(Hospital hospital) {
            hospitalMapper.updateHos(hospital);
    }

    public List<Hospital> findHosByLikeName(String hosName, String hosLevel) {
            return hospitalMapper.findHosByLikeName(hosName,hosLevel);
    }
}
