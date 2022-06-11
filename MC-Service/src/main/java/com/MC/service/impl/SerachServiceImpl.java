package com.MC.service.impl;

import com.MC.service.PostService;
import com.MC.service.SerachService;
import com.MC.entity.PostDetai;
import com.MC.entity.User;
import com.MC.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
