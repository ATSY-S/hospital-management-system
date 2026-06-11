package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.service.DepartmentService;
import com.hy.service.RegisterService;
import com.hy.service.ChargeService;
import com.hy.service.PatientService;
import com.hy.mapper.HospitalMapper;
import com.hy.pojo.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private HospitalMapper hospitalMapper;

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> data = new HashMap<>();
        
        // 今日挂号统计
        int todayRegister = registerService.getTodayRegisterCount();
        int yesterdayRegister = registerService.getYesterdayRegisterCount();
        data.put("todayRegister", todayRegister);
        data.put("todayRegisterTrend", calculateTrend(todayRegister, yesterdayRegister));
        
        // 在院患者统计
        int inHospital = patientService.getInHospitalCount();
        int yesterdayInHospital = patientService.getYesterdayInHospitalCount();
        data.put("inHospital", inHospital);
        data.put("inHospitalTrend", calculateTrend(inHospital, yesterdayInHospital));
        
        // 今日处方数统计
        int todayPrescription = chargeService.getTodayPrescriptionCount();
        int yesterdayPrescription = chargeService.getYesterdayPrescriptionCount();
        data.put("todayPrescription", todayPrescription);
        data.put("todayPrescriptionTrend", calculateTrend(todayPrescription, yesterdayPrescription));
        
        // 今日营收统计
        BigDecimal todayRevenue = chargeService.getTodayRevenue();
        BigDecimal yesterdayRevenue = chargeService.getYesterdayRevenue();
        data.put("todayRevenue", todayRevenue);
        data.put("todayRevenueTrend", calculateTrend(todayRevenue.doubleValue(), yesterdayRevenue.doubleValue()));
        
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/registerTrend")
    public Result getRegisterTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> trend = registerService.getRegisterTrend(days);
        return new Result("200", "查询成功", trend);
    }

    @GetMapping("/departmentCount")
    public Result getDepartmentCount() {
        List<Map<String, Object>> count = registerService.getDepartmentCount();
        return new Result("200", "查询成功", count);
    }

    @GetMapping("/departmentVisit")
    public Result getDepartmentVisit() {
        List<Map<String, Object>> count = registerService.getDepartmentCount();
        return new Result("200", "查询成功", count);
    }

    @GetMapping("/ageDistribution")
    public Result getAgeDistribution() {
        List<Map<String, Object>> distribution = patientService.getAgeDistribution();
        return new Result("200", "查询成功", distribution);
    }

    @GetMapping("/hospitalStatus")
    public Result getHospitalStatus() {
        List<Hospital> hospitals = hospitalMapper.findHosList();
        int normal = 0, pause = 0, top = 0;
        for (Hospital h : hospitals) {
            if ("正常".equals(h.getHospitalStatus())) normal++;
            else pause++;
            if ("三级甲等".equals(h.getHospitalLevel())) top++;
        }
        Map<String, Integer> data = new HashMap<>();
        data.put("normal", normal);
        data.put("pause", pause);
        data.put("top", top);
        return new Result("200", "查询成功", data);
    }

    private String calculateTrend(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? "+100%" : "0%";
        }
        double trend = ((current - previous) / previous) * 100;
        String sign = trend >= 0 ? "+" : "";
        return sign + String.format("%.1f%%", trend);
    }

    private Map<String, Object> createMap(String k1, Object v1, String k2, Object v2) {
        Map<String, Object> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}
