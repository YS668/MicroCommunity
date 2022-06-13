package com.MC.controller;

import com.MC.dto.ReportDto;
import com.MC.entity.Msg;
import com.MC.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ReportController {
    @Autowired
    ReportService reportService;
    @PostMapping("/addReport")
    public Msg addReport(@RequestBody ReportDto dto){
        boolean b = reportService.addReport(dto);
        return b?Msg.success("举报成功！"):Msg.fail("你已经举报过了！");
    }
}
