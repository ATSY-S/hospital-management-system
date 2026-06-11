package com.hy.service;

import com.hy.mapper.MedicineMapper;
import com.hy.pojo.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicineService {
    @Autowired
    private MedicineMapper medicineMapper;

    public List<Medicine> list(String name, String code, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return medicineMapper.findByPage(name, code, offset, size);
    }

    public Long count(String name, String code) {
        return medicineMapper.count(name, code);
    }

    public Medicine getById(Long id) {
        return medicineMapper.findById(id);
    }

    public void create(Medicine medicine) {
        medicineMapper.insert(medicine);
    }

    public void update(Medicine medicine) {
        medicineMapper.update(medicine);
    }

    public void delete(Long id) {
        medicineMapper.delete(id);
    }
}
