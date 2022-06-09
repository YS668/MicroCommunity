package com.zyh.controller;


import com.zyh.entity.Answer;
import com.zyh.entity.Comment;
import com.zyh.entity.Msg;
import com.zyh.service.CommentService;
import com.zyh.sysLog.Logweb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

/**
 * 评论相关的接口
 */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     *
     * @param id  评论文章的ID
     * @param map 接受json文档，也就是获取评论内容
     * @return
     */
    @Logweb("评论")
    @PostMapping("/comment/{id}")
    public Msg comment(@PathVariable Integer id, @RequestBody Map<String, String> map){
        Comment item = commentService.comment(id, map.get("content"));
        return Msg.success("评论成功").add("item",item);
    }

    /**
     *
     * @param id 查出一篇文档里的所有评论
     * @return
     */
    @GetMapping("/findCommentByPid/{id}")
    public Msg findCommentByPid(@PathVariable Integer id){
        List<Comment> commentList = commentService.findCommentByPid(id);
        return Msg.success().add("list",commentList);
    }

    /**
     *
     * @param commentId 回复的评论ID
     * @param map 接收回复内容的
     * @return
     */
    @Logweb("回复")
    @PostMapping("/answer/{commentId}")
    public Msg answer(@PathVariable Integer commentId,@RequestBody Map<String,String> map){
        Answer answer = commentService.answer(commentId, map.get("content"));
        return Msg.success().add("answer",answer);
    }
}
