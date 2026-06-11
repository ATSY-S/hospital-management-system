package com.hy.mapper;

import com.hy.pojo.Department;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DepartmentMapper {
    List<Department> findAll();
    Department findById(@Param("id") Long id);
}
