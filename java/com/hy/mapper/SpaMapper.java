package com.hy.mapper;

import java.util.List;
import java.util.Map;

public interface SpaMapper {
     Integer getHosCount();

     Integer getPatCount();

     List<Map<String, Integer>> getHosPatCount();

     Integer getSJHosCount();

     List<Map<String, Integer>> getPatAge();

     Integer getPatSex();
}
