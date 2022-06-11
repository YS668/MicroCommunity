package com.MC.controller;

import com.MC.entity.Msg;
import com.MC.service.InteractionService;
import com.MC.sysLog.Logweb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 点赞收藏相关的接口
 */
@RestController
public class InteractionController {
    @Autowired
    InteractionService interactionService;

    @Logweb("点赞")
    @GetMapping("/onStart/{postId}")
    public Msg onStart(@PathVariable Integer postId){
        if(interactionService.existsStar(postId)){
            return Msg.fail("点赞失败");
        }
        interactionService.onStart(postId);
        return Msg.success("点赞成功");
    }
    @Logweb("取消点赞")
    @GetMapping("/unStart/{postId}")
    public Msg unStart(@PathVariable Integer postId){
        if(!interactionService.existsStar(postId)){
            return Msg.fail("取消点赞失败");
        }
        interactionService.unStart(postId);
        return Msg.success("取消点赞成功");
    }
    @Logweb("收藏")
    @GetMapping("/onCollect/{postId}")
    public Msg onCollect(@PathVariable Integer postId){
        if(interactionService.existsCollect(postId)){
            return Msg.fail("收藏失败");
        }
        else {
            interactionService.onCollect(postId);
        }
        return Msg.success("收藏成功");
    }
    @Logweb("取消收藏")
    @GetMapping("/unCollect/{postId}")
    public Msg unCollect(@PathVariable Integer postId){
        if(!interactionService.existsCollect(postId)){
            return Msg.fail("取消收藏失败");
        }
        else {
            interactionService.unCollect(postId);
        }
        return Msg.success("取消收藏成功");
    }



}
