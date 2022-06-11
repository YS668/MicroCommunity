package com.MC.service.impl;

import com.MC.service.ReportService;
import com.MC.dto.ReportDto;
import com.MC.entity.Report;
import com.MC.mapper.ReportMapper;
import com.MC.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Override
    public boolean addReport(ReportDto reportDto) {
        Integer userId = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        Report t = reportMapper.findReport(userId, reportDto.getTo_id(), reportDto.getType());
        if(t!=null)
            return false;
        Report report=new Report();
        report.setFrom_uid(userId);
        report.setTo_id(reportDto.getTo_id());
        report.setContent(reportDto.getContent());
        report.setType(reportDto.getType());
        report.setTime(new Date());
        reportMapper.addReport(report);
        return true;
    }
}
