package com.zyh.service;

import com.zyh.dto.FindPwdSendEmailDto;
import com.zyh.dto.RegisterDto;
import com.zyh.dto.UpdatePasswordDto;
import com.zyh.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String,String> register(RegisterDto registerDto);
    int exists(String username);
    User findUserByUserName(String username);

    User findUserDetails(String username);

    List<User> findFollowerSimpleInfo(List<Integer> list);

    boolean updateUserName(String userName);

    void  updateSex(Integer sex);

    void updateInfo(String info);

    Map<String,String> updatePassword(UpdatePasswordDto dto);

    Map<String,String> findPassword(FindPwdSendEmailDto dto,String ip) ;




}
