package com.MC.service;

import com.github.pagehelper.PageInfo;
import com.MC.entity.ReportDeatil;
import com.MC.entity.User;

import java.util.List;

public interface AdminService {
    User findById(Integer id);
    PageInfo findAll(Integer page, Integer size);

    boolean changeStatus(Integer id);

    List<ReportDeatil> findAllReport();

    void solve(Integer id);

    void deleteSolve(Integer id);
}
