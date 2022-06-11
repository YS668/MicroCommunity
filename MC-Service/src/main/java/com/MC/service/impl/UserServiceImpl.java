package com.MC.service.impl;

import com.MC.service.EmailService;
import com.MC.service.InteractionService;
import com.MC.service.UserService;
import com.MC.dto.FindPwdSendEmailDto;
import com.MC.dto.RegisterDto;
import com.MC.dto.UpdatePasswordDto;
import com.MC.entity.LimitIp;
import com.MC.entity.User;
import com.MC.mapper.UserMapper;
import com.MC.utils.IpLimitUtil;
import com.MC.utils.JWTUtil;
import com.MC.utils.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    InteractionService interactionService;
    @Lazy
    @Autowired
    RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    EmailService emailService;
    @Lazy
    @Autowired
    IpLimitUtil ipLimitUtil;

    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @Override
    public Map<String,String> register(RegisterDto registerDto) {
        Map<String,String> map=new HashMap<>();
       if(!registerDto.getPassword().equals(registerDto.getSecondPassword())){
           map.put("message","两次密码不一致");
           return map;
        }
        //获取验证码
        String code = (String) redisTemplate.opsForValue().get(registerDto.getEmail() + ":code");

        if(code==null||code.equals("")){
            map.put("message","验证码已过期");
            return map;
        }

        if(!code.equals(registerDto.getCode())) {
            map.put("message", "验证码不正确");
            return map;
        }
        if(exists(registerDto.getUsername())==0){//查看用户名是否已经存在，如果没存在
            //生产随机的salt
            String salt=SaltUtil.getSalt(5);
            //加密方式
            Md5Hash md5Hash=new Md5Hash(registerDto.getPassword(),salt,1024);
            String password=md5Hash.toHex();
            //默认用户头像
            String avatar="http://rd7wsmibf.hn-bkt.clouddn.com/four.jpg";
            userMapper.register(registerDto.getUsername(),password,salt,avatar,registerDto.getEmail());
            //删除验证秒
            redisTemplate.delete(registerDto.getEmail() + ":code");
            map.put("message","注册成功");
            return map;
        }
        map.put("message","用户已存在");
        return map;
    }

    /**
     * 查找用户名是否存在
     * @param username
     * @return
     */
    @Override
    public int exists(String username) {
        return userMapper.exists(username);
    }

    /**
     * 通过用户名找出用户
     * @param username
     * @return
     */
    @Override
    public User findUserByUserName(String username) {

        User userByUserName = userMapper.findUserByUserName(username);

        return userByUserName;
    }

    /**
     * 找出用户的详细信息  例如粉丝、关注、收藏数量
     * @param username
     * @return
     */
    @Override
    public User findUserDetails(String username) {
        User user=userMapper.findUserByUserName(username);
        if(user==null) return null;
        user.setPassword(null);
        user.setSalt(null);
        user.setProfile(interactionService.findUserProfile(user.getId()));
        return user;
    }

    /**
     * 找出粉丝的信息：用户id、用户名、头像这些
     * @param list
     * @return
     */
    @Override
    public List<User> findFollowerSimpleInfo(List<Integer> list) {
       return userMapper.findFollowerSimpleInfo(list);
    }

    /**
     * 修改用户名
     * @param userName
     * @return
     */
    @Override
    public boolean updateUserName(String userName) {
        int exists = userMapper.exists(userName);
        if(exists==0){//如果要修改的用户名没被使用
            Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
            User user=new User();
            user.setId(userId);
            user.setUsername(userName);
            userMapper.updateUserInfo(user);
            return true;
        }
        return false;
    }

    /**
     * 修改性别
     * @param sex
     */
    @Override
    public void updateSex(Integer sex) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=new User();
        user.setId(userId);
        user.setSex(sex);
        userMapper.updateUserInfo(user);
    }

    /**
     * 修改个人主页的简介
     * @param info
     */
    @Override
    public void updateInfo(String info) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=new User();
        user.setId(userId);
        user.setPrivate_info(info);
        userMapper.updateUserInfo(user);
    }

    /**
     * 修改密码
     * @param dto
     * @return
     */
    @Override
    public Map<String,String> updatePassword(UpdatePasswordDto dto) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=userMapper.findUserById(userId);
        String lastPassword = dto.getLastPassword();
        Md5Hash md5Hash=new Md5Hash(lastPassword,user.getSalt(),1024);
        lastPassword=md5Hash.toHex();
        Map<String,String> map=new HashMap<>();
        if(user.getPassword().equals(lastPassword)){
            if(dto.getNewPassword().equals(dto.getSencondPassword())){
                String salt=SaltUtil.getSalt(5);
                String newPassword=new Md5Hash(dto.getNewPassword(),salt,1024).toHex();
                User newUser=new User();
                newUser.setId(user.getId());
                newUser.setSalt(salt);
                newUser.setPassword(newPassword);
                userMapper.updateUserInfo(newUser);
                map.put("message","success");
            }
            else{
                map.put("message","两次密码不一致");
            }
        }
        else map.put("message","旧密码错误");
        return map;
    }

    /**
     * 找回密码
     * @param dto
     * @param ip
     * @return
     */
    public Map<String, String> findPassword(FindPwdSendEmailDto dto,String ip) {
        //限流
        ArrayList<LimitIp> ipArrayList=new ArrayList<>();
        ipArrayList.add(new LimitIp(ip+"findPsw",60*60,5,"一小时"));
        ipArrayList.add(new LimitIp(ip+"findPsw:count",60,1,"一分钟"));
        Map<String, String> msg = ipLimitUtil.ipContro(ipArrayList);
        if(!msg.get("message").equals("success"))
            return msg;

        Map<String,String> map=new HashMap<>();
        User user=userMapper.findUserByUserName(dto.getUsername());
        if(user==null){
            map.put("message","用户不存在");
            return map;
        }
        if(!user.getEmail().equals(dto.getEmail())){
            map.put("message","输入邮箱与该用户绑定邮箱不一致");
            return map;
        }
        String newPsw=SaltUtil.getSalt(8);
        //发送邮件
        emailService.sendPsw(dto.getEmail(),newPsw);
        String newSalt=SaltUtil.getSalt(6);
        newPsw=new Md5Hash(newPsw,newSalt,1024).toHex();
        User newUser=new User();
        newUser.setId(user.getId());
        newUser.setSalt(newSalt);
        newUser.setPassword(newPsw);

        //修改密码
        userMapper.updateUserInfo(newUser);
        map.put("message","success");
        return map;
    }




}
