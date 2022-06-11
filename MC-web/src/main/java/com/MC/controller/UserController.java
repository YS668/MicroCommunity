package com.MC.controller;


import com.MC.dto.FindPwdSendEmailDto;
import com.MC.dto.LoginDto;
import com.MC.dto.RegisterDto;
import com.MC.dto.UpdatePasswordDto;
import com.MC.entity.Msg;
import com.MC.entity.User;
import com.MC.service.UserService;
import com.MC.sysLog.Logweb;
import com.MC.utils.IpUtil;
import com.MC.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息的修改
 * 注册登录
 */
@RestController
@Api(value = "UserController")
public class UserController {


    @Autowired
    UserService userService;

    @Logweb("用户注册")
    @ApiOperation(value = "用户注册", notes = "填写用户名，密码")
    @PostMapping("/register")
    public Msg register( @Validated @RequestBody RegisterDto registerDto)
    {
        Map<String,String> info= userService.register(registerDto);
        String msg=info.get("message");
        if (msg.equals("注册成功"))
            return Msg.success(msg);
        return Msg.fail(msg);
    }

    @Logweb("用户登录")
    @ApiOperation(value = "用户登录", notes = "登录--不进行拦截")
    @PostMapping("/login")
    public Msg login(@RequestBody @Validated LoginDto loginDto) {
        User user=userService.findUserByUserName(loginDto.getUsername());
        if(user==null)
            return  Msg.fail("用户名不存在");
        if(user.getStatus()==0)
            return Msg.fail("用户被锁定");
        String password=loginDto.getPassword();
        //加密方式 md5+哈希散列1024次
        Md5Hash md5Hash=new Md5Hash(password,user.getSalt(),1024);
        password=md5Hash.toHex();
        if(password.equals(user.getPassword())){
            user.setPassword("你看不到的");
            user.setSalt("你还是看不到");
            //成功反回token
            return Msg.success("登录成功").add("token", JWTUtil.createToken(user.getId())).add("user",user);
        }
        return Msg.fail("密码错误");
    }


    @GetMapping("/getAvatar/{username}")
    public String getAvatar(@PathVariable String username){
        User user=userService.findUserByUserName(username);
        return user.getAvatar();
    }
//因为是前后端分离，退出登录由前端来做就好了
//    @ApiOperation(value = "退出登录", notes = "需要登录才行放行")
//    @GetMapping("/logout")
//    public Msg logout(){
//        SecurityUtils.getSubject().logout();
//        return Msg.success("退出成功");
//    }


    @GetMapping("/findUserDetails/{username}")
    public Msg findUserDetails(@PathVariable String username){
        return Msg.success().add("user",userService.findUserDetails(username));
    }

    @Logweb("修改用户名")
    @GetMapping("/updateUserName")
    public Msg updateUserName(@RequestParam(required = true)  String userName){
        if(userName!=null&&userName.length()>=3&&userName.length()<=20){
            boolean b = userService.updateUserName(userName);
            if(b){
                return Msg.success("修改成功");
            }
            return Msg.fail("用户名已存在");
        }
        return Msg.fail("格式错误");
    }

    @Logweb("修改性别")
    @GetMapping("/updateSex")
    public Msg updateSex(@RequestParam(required = true) Integer sex){
        if(sex==1||sex==2){
            userService.updateSex(sex);
            return Msg.success("修改成功");
        }
        return Msg.success("输入有误");
    }

    @Logweb("修改个人简介")
    @GetMapping("/updateInfo")
    public Msg  updateInfo(@RequestParam(required = true) String info){
        userService.updateInfo(info);
        return Msg.success("修改成功");
    }

    @Logweb("修改密码")
    @PostMapping("/updatePassword")
    public Msg updatePassword(@RequestBody @Validated UpdatePasswordDto dto) {
        Map<String, String> map = userService.updatePassword(dto);
        String message = map.get("message");
        if (message.equals("success"))
            return Msg.success(message);
        return Msg.fail(message);
    }
    @Logweb("找回密码")
    @PostMapping("/findPassword")
    public Msg findPassword(@Validated @RequestBody FindPwdSendEmailDto findPwdSendEmailDto, HttpServletRequest request) throws MessagingException {
        String ipAddr = IpUtil.getIpAddr(request);
        Map<String, String> msg = userService.findPassword(findPwdSendEmailDto, ipAddr);
        if(msg.get("message").equals("success"))
        {
            return Msg.success("邮件已发送");
        }
        else
            return Msg.fail(msg.get("message"));
//        asyncService.hello();
//        return Msg.success();

    }


    @ApiOperation(value = "测试无权限", notes = "无权限跳转的接口")
    @GetMapping(path = "/unauthorized/{message}")
    public Msg unauthorized(@PathVariable String message) {
        return Msg.fail().add("info", message);
    }

    /**
     * 就是config配置类FilterExceConfig 认证异常的捕获
     * @param request
     * @return
     */
    @Logweb("认证失败")
    @RequestMapping("/error/exthrow")
    public Map<String, Object> rethrow(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "401");
        map.put("message", "token认证失败");
        return map;
    }
}
