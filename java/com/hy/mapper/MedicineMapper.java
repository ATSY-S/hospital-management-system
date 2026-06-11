package com.hy.mapper;

import com.hy.pojo.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MedicineMapper {
    List<Medicine> findByPage(@Param("name") String name, @Param("code") String code, @Param("offset") Integer offset, @Param("size") Integer size);
    Long count(@Param("name") String name, @Param("code") String code);
    Medicine findById(@Param("id") Long id);
    void insert(@Param("med") Medicine medicine);
    void update(@Param("med") Medicine medicine);
    void delete(@Param("id") Long id);
}
