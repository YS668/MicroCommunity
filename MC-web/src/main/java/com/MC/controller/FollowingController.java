package com.MC.controller;

import com.MC.entity.Msg;
import com.MC.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 关注相关的接口
 */
@CrossOrigin
@RestController
@RequestMapping("/following")
public class FollowingController {
    @Autowired
    FollowingService followingService;

    /**
     * 找到关注的人发的所有文章
     * @param page 分页的页数，第几页 默认是1
     * @param size 一页的size        默认是5
     * @return
     */
    @GetMapping("/findPost")
    public Msg findFollowingPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                                 @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return Msg.success().add("postList",followingService.findFollowingPost(page, size));
    }

    /**
     * 找出已经关注的所有用户
     * @return
     */
    @GetMapping("/findFowings")
    public Msg findFowings(){
        return Msg.success().add("followers",followingService.findFowings());
    }
}
