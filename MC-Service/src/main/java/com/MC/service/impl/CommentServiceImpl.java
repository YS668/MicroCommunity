package com.MC.service.impl;

import com.MC.service.CommentService;
import com.MC.service.MessageService;
import com.MC.entity.Answer;
import com.MC.entity.Comment;
import com.MC.entity.PostDetai;
import com.MC.entity.User;
import com.MC.mapper.CommentMapper;
import com.MC.mapper.PostMapper;
import com.MC.mapper.UserMapper;
import com.MC.utils.JWTUtil;
import com.MC.utils.ReplaceUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    MessageService messageService;
    @Autowired
    PostMapper postMapper;

    /**
     * 添加评论
     * @param pid  文章id
     * @param content 评论内容
     * @return
     */
    @Override
    public Comment comment(Integer pid, String content) {
        Comment comment=new Comment();
        comment.setPid(pid);
        comment.setContent(content);

        //通过token获取当前用户id
        int uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        comment.setUid(uid);
        comment.setTime(new Date());
        User user=userMapper.findUserById(uid);
        comment.setUname(user.getUsername());
        comment.setAvatar(user.getAvatar());

        //添加评论
        commentMapper.addComment(comment);

        //评论消息，消息推送
        PostDetai postDetai=postMapper.findPostById(pid);
        //替换成超链接
        String message=ReplaceUtil.userReplace(user.getUsername())+"在"+ReplaceUtil.PostReplace(pid,postDetai.getTitle())
                       +"留下了评论";
        messageService.addMessage(postDetai.getUid(),message,1);
        return comment;
    }

    /**
     * 找出这篇文章所有的评论
     * @param id
     * @return
     */
    @Override
    public List<Comment> findCommentByPid(Integer id) {
        List<Comment> commentList=commentMapper.findCommentByPid(id);
        for(Comment c:commentList){
            //找出这条评论的回复内容
            c.setAnswerList(commentMapper.findAnswerByCommentId(c.getId()));
        }
        return commentList;
    }

    /**
     * 回复一条评论
     * @param commentId 评论id
     * @param content   内容
     * @return
     */
    @Override
    public Answer answer(Integer commentId, String content) {
        Answer answer=new Answer();
        //获取回复这个用户的信息
        User user=userMapper.findUserById(JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        answer.setUid(user.getId());
        answer.setUname(user.getUsername());
        answer.setAvatar(user.getAvatar());
        answer.setTime(new Date());
        answer.setContent(content);
        answer.setComment_id(commentId);
        //添加回复
        commentMapper.answer(answer);
        //回复消息
        messageService.addMessage(2,commentId,user.getUsername());
        return answer;
    }





}
