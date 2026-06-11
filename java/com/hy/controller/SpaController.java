package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.service.SpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spa")

public class SpaController {

    @Autowired
    private SpaService spaService;

    //获取医院总数
    @GetMapping("/getHosCount")
    public Result getHosCount(){
        Integer count = spaService.getHosCount();
        return new Result("200","查询成功",count);
    }

    //获取患者信息
    @GetMapping("/getPatCount")
    public Result getPatCount() {
        Integer count = spaService.getHosCount();
        return new Result("200", "查询成功", count);
    }

    //获取医院患者总数
    @GetMapping("/getHosPatCount")
    public Result getHosPatCount(){
        //返回医院ID
        List<Map<String,Integer>> list = spaService.getHosPatCount();
        return new Result("200","查询成功",list);
    }

    //获取三甲医院数量
    @GetMapping("/getSJHosCount")
    public Result getSJHosCount(){
        Integer count = spaService.getSJHosCount();
        return new Result("200","查询成功",count);
    }

    //获取患者年龄分布
    @GetMapping("/getPatAge")
    public Result getPatAge(){
        List<Map<String,Integer>> list = spaService.getPatAge();
        return new Result("200","查询成功",list);
    }

    //获取男性患者数量
    @GetMapping("/getPatSex")
    public Result getPatSex(){
        Integer count = spaService.getPatSex();
        return new Result("200","查询成功",count);
    }

}
