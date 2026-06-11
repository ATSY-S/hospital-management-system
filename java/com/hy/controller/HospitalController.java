package com.hy.controller;


import com.hy.pojo.Hospital;
import com.hy.pojo.Result;
import com.hy.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hos")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //查询所有医院信息
    @GetMapping("/findHosList")
    public Result findHosList(){
        List<Hospital> hoss = hospitalService.findHosList();
        return new Result("200","查询成功",hoss);

    }

    //增加医院信息
    @PostMapping("/insertHos")
    public Result insertHos(@RequestBody Hospital hospital){
        //首先判断录入医院名称在数据库是否重名
        String hospitalName = hospital.getHospitalName();
        Hospital hos = hospitalService.selectHosByName(hospitalName);
        if(hos!=null){
            return new Result("1001","医院已经存在，无法添加",null);
        }else{
            hospitalService.insertHos(hospital);
            return new Result("201","添加成功","null");
        }
    }

    //删除
    @DeleteMapping("/deleteById")
    public Result deleteById(Integer id){
        hospitalService.deleteById(id);
        return new Result("202","删除成功",null);
    }

    //批量删除
    @DeleteMapping("/deleteByIds")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        hospitalService.deleteByIds(ids);
        return new Result("203","批量删除成功",null);
    }

    //更新医院信息
    @PutMapping("/updateHos")
    public Result updateHos(@RequestBody Hospital hospital){
        hospitalService.updateHios(hospital);
        return new Result("204","更新成功",null);
    }

    //模糊查询
    @GetMapping("/findHosByLikeName")
    public Result findHosByLikeName(String hosName,String hosLevel){
        List<Hospital> hoss = hospitalService.findHosByLikeName(hosName,hosLevel);
        return new Result("205","模糊查询成功",hoss);

    }

}
