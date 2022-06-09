package com.zyh.controller;


import com.zyh.entity.Mention;
import com.zyh.entity.Msg;
import com.zyh.entity.User;
import com.zyh.service.MentionService;
import com.zyh.service.PostService;
import com.zyh.service.ProfileService;
import com.zyh.service.UserService;
import com.zyh.sysLog.Logweb;
import com.zyh.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 个人主页相关的接口
 */
@RequestMapping("/profile")
@RestController
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    MentionService mentionService;

    /**
     * 个人主页的接口，返回这个用户相关信息
     * @param username 用户名
     * @return
     */
    @GetMapping("/findUserProfile/{username}")
    public Msg findUserProfile(@PathVariable String username){
        User user=userService.findUserDetails(username);
        if(user==null)
            return Msg.fail("用户名不存在");
        boolean isfollower=profileService.isFollower(user.getId(),JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        return Msg.success().add("user",user).add("isfollower",isfollower);
    }

    /**
     * 关注，当前请求接口的用户，关注这个id的用户
     * @param userId 被关注的人id
     * @return
     */
    @Logweb("关注")
    @GetMapping("/follow/{userId}")
    public Msg follow(@PathVariable Integer userId){
        boolean result = profileService.follow(userId);
        if(!result)return Msg.fail("关注失败");
        return Msg.success("关注成功");
    }

    /**
     * 取消关注，当前请求接口的用户，取消关注这个id的用户
     * @param userId
     * @return
     */
    @Logweb("取消关注")
    @GetMapping("/unFollow/{userId}")
    public Msg unfollow(@PathVariable Integer userId){
        boolean result = profileService.unFollow(userId);
        if(!result)return Msg.fail("取消关注失败");
        return Msg.success("取消关注成功");
    }

    /**
     * 个人主页，查看这个用户的动态（也就是发过的文章）
     * @param id 这个用户id
     * @return
     */
    @GetMapping("/findPostById/{id}")
    public Msg findPostByUserId(@PathVariable Integer id){
        return Msg.success().add("postList",postService.findPostByUserId(id));
    }

    /**
     * 删除文章，在个人主页删除文章
     * @param map
     * @return
     */
    @Logweb("删除文章")
    @PutMapping("/delete")
    public Msg deletePost(@RequestBody Map map){
        boolean result = postService.delete((Integer) map.get("id"));
        if(result){
            return Msg.success("删除成功");
        }
        return Msg.fail("删除失败");
    }

    /**
     * 个人主页，收藏的所有文章
     * @param id 用户id
     * @return
     */
    @GetMapping("/findCollect/{id}")
    public Msg findCollectById(@PathVariable  Integer id){
        return Msg.success(profileService.findCollectById(id));
    }

    /**
     * 个人主页，查出这个用户的粉丝
     * @param id
     * @return
     */
    @GetMapping("/findFollowers/{id}")
    public Msg findFollowers(@PathVariable Integer id){
        List<User> followers = profileService.findFollowers(id);
        return Msg.success().add("followers",followers);
    }

    /**
     * 个人主页，查出这个用户的关注
     * @param id
     * @return
     */
    @GetMapping("/findFollowings/{id}")
    public Msg findFollowings(@PathVariable Integer id){
        List<User> followings = profileService.findFollowings(id);
        return Msg.success().add("followers",followings);
    }

    /**
     * 个人主页，查出这个用户所有被提到的
     * 也就是被别人@
     * @param id
     * @return
     */
    @GetMapping("/findMentionsByUid/{id}")
    public Msg findMentionsByUid(@PathVariable Integer id){
        List<Mention> list=mentionService.findMentionsByUid(id);
        return Msg.success().add("list",list);
    }

    /**
     * 查看某一个被提到的具体内容
     * @param id
     * @return
     */
    @GetMapping("/findMentionById/{id}")
    public Msg findMentionById(@PathVariable Integer id){
        Mention mention=mentionService.findMentionById(id);
        return Msg.success().add("data",mention);
    }

    /**
     * 删除被提到的
     * @param mention
     * @return
     */
    @Logweb("删除提醒")
    @PutMapping("/deleteMention")
    public Msg deleteMention(@RequestBody Mention mention){

        if(mentionService.deleteMention(mention))
            return Msg.success();
        return Msg.fail();
    }
}
