package com.MC.service.impl;


import com.MC.service.MessageService;
import com.MC.service.PostService;
import com.MC.service.ProfileService;
import com.MC.entity.PostDetai;
import com.MC.entity.User;
import com.MC.mapper.UserMapper;
import com.MC.utils.JWTUtil;
import com.MC.utils.TimeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    PostService postService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MessageService messageService;

    //user2是否关注了user1
    @Override
    public boolean isFollower(Integer user1, Integer user2) {
        return redisTemplate.opsForSet().isMember(user1+":followers",user2).booleanValue();
    }

    /**
     * 当前用户关注 userId这个用户
     * @param userId
     * @return
     */
    @Override
    public boolean follow(Integer userId) {
        Integer uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        boolean isFollower=isFollower(userId,uid);
        if(!isFollower){
            redisTemplate.opsForSet().add(userId+":followers",uid);
            redisTemplate.opsForSet().add(uid+":followings",userId);
            messageService.addFollow(uid,userId,5);
        }
        return !isFollower;
    }

    /**
     * 取消关注
     * @param userId
     * @return
     */
    @Override
    public boolean unFollow(Integer userId) {
        Integer uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        boolean isFollower=isFollower(userId,uid);
        if(isFollower){
            redisTemplate.opsForSet().remove(userId+":followers",uid);
            redisTemplate.opsForSet().remove(uid+":followings",userId);
        }
        return isFollower;
    }

    /**
     * 找出某用户的所有收藏（文章）
     * @param id
     * @return
     */
    @Override
    public Map findCollectById(Integer id) {
        Set<ZSetOperations.TypedTuple<Integer>> set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(id + ":collect",  0,Long.MAX_VALUE);
        List<PostDetai> list=new ArrayList<>();
        List<String> collectTime=new ArrayList<>();
        for(ZSetOperations.TypedTuple<Integer> x: set){
            list.add(postService.findPostById(x.getValue()));
            collectTime.add(TimeUtil.stampToDate(x.getScore().longValue()));
        }
        HashMap map=new HashMap();
        map.put("postList",list);
        map.put("collectTime",collectTime);
        return map;
    }

    /**
     * 找出所有粉丝
     * @param id
     * @return
     */
    @Override
    public List<User> findFollowers(Integer id) {
        Set<Integer> members = redisTemplate.opsForSet().members(id + ":followers");
        if(members.size()==0)return new ArrayList<>();
        List<Integer> list=new ArrayList<>(members);
        return userMapper.findFollowerSimpleInfo(list);
    }

    /**
     * 找出所有关注
     * @param id
     * @return
     */
    @Override
    public List<User> findFollowings(Integer id) {
        Set<Integer> members = redisTemplate.opsForSet().members(id + ":followings");
        if(members.size()==0)return new ArrayList<>();
        List<Integer> list=new ArrayList<>(members);
        return userMapper.findFollowerSimpleInfo(list);
    }
}
