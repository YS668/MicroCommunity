package com.MC.service;

import com.MC.entity.Answer;
import com.MC.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment comment(Integer pid,String content);

    List<Comment> findCommentByPid(Integer id);

    Answer answer(Integer commentId,String content);


}
