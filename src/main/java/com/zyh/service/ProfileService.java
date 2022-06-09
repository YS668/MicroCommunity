package com.zyh.service;

import com.zyh.entity.PostDetai;
import com.zyh.entity.User;

import java.util.List;
import java.util.Map;

public interface ProfileService {
    boolean isFollower(Integer user1,Integer user2);
    boolean follow(Integer userId);

    boolean unFollow(Integer userId);

    Map findCollectById(Integer id);
    List<User> findFollowers(Integer id);

    List<User> findFollowings(Integer id);
}
