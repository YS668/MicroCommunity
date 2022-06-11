package com.MC.service;

public interface EmailService {
    void sendRegisterEmail(String toEmail) ;
    void sendPsw(String email,String psw) ;
}
