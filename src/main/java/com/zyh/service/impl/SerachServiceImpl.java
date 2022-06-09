package com.zyh.service.impl;

import com.zyh.entity.PostDetai;
import com.zyh.entity.User;
import com.zyh.mapper.UserMapper;
import com.zyh.service.PostService;
import com.zyh.service.SerachService;
import com.zyh.sysLog.Logweb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Transactional
public class SerachServiceImpl implements SerachService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PostService postService;

    /**
     * 通过关键字 找出所有文章和用户
     * @param content
     * @return
     */
    @Override
    public Map<String, List> serach(String content) {
        List<User> users = userMapper.serachUser("%" + content + "%");
        List<PostDetai> postDetais = postService.serachPost(content);
        Map<String,List> map=new HashMap<>();
        map.put("users",users);
        map.put("post",postDetais);
        return map;
    }
}
