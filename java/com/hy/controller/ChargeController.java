package com.hy.controller;

import com.hy.pojo.Result;
import com.hy.pojo.Charge;
import com.hy.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charge")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String patientName,
                       @RequestParam(required = false) String chargeType,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String startTime,
                       @RequestParam(required = false) String endTime) {
        LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
        LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;
        List<Charge> list = chargeService.list(patientName, chargeType, status, start, end, page, size);
        Long total = chargeService.count(patientName, chargeType, status, start, end);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return new Result("200", "查询成功", data);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Charge charge = chargeService.getById(id);
        return new Result("200", "查询成功", charge);
    }

    @PostMapping
    public Result create(@RequestBody Charge charge) {
        chargeService.create(charge);
        return new Result("200", "创建成功", null);
    }

    @PutMapping("/{id}/pay")
    public Result pay(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String paymentMethod = params.get("paymentMethod");
        chargeService.pay(id, paymentMethod);
        return new Result("200", "支付成功", null);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        chargeService.delete(id);
        return new Result("200", "删除成功", null);
    }
}
