package com.zyh.service;

import com.zyh.entity.Answer;
import com.zyh.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment comment(Integer pid,String content);

    List<Comment> findCommentByPid(Integer id);

    Answer answer(Integer commentId,String content);


}
