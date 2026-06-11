package com.hy.controller;


import com.hy.pojo.Patient;
import com.hy.pojo.Result;
import com.hy.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pat")

public class PatientController {
    @Autowired
    private PatientService patientService;
    //查询所有患者信息
    @GetMapping("/findPatList")
    public Result findPatList(){
        List<Patient> list = patientService.findPatList();
        return new Result("200","查询成功",list);
    }

    //添加患者信息
    @PostMapping("/insertPat")
    public Result insertPat(@RequestBody Patient patient){
        patientService.insertPat(patient);
        return new Result("201","🀄增添成功",null);

    }

    //更新
    @PutMapping("/updatePat")
    public Result updatePat(@RequestBody Patient patient){
        patientService.updatePat(patient);
        return new Result("204","更新成功",null);
    }

    //删除
    @DeleteMapping("/deletePat")
    public Result deletePat(Integer id){
        patientService.deletePat(id);
        return new Result("202","删除成功",null);
    }

    //根据患者的名字模糊查询
    @GetMapping("/findPatByName")
    public Result findPatByName(String patName){
        List<Patient> list = patientService.findPatByNamme(patName);
        return new Result("200","查询成功",list);
    }

}
