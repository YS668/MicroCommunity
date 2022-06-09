package com.zyh.mapper;

import com.zyh.entity.Answer;
import com.zyh.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    @Insert("insert into comment(uid,uname,avatar,time,content,pid) values(#{uid},#{uname},#{avatar},#{time},#{content},#{pid})")
    void addComment(Comment comment);

    @Select("select * from comment where pid=#{id} order by time desc")
    List<Comment> findCommentByPid(Integer id);

    @Insert("insert into answer(uid,uname,avatar,content,time,comment_id) values (#{uid},#{uname},#{avatar},#{content},#{time},#{comment_id})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void answer(Answer answer);

    @Select("select * from answer where comment_id=#{id} order by time desc")
    List<Answer> findAnswerByCommentId(int id);

    @Select("select * from comment where id=#{commentId}")
    Comment findByCommentId(Integer commentId);
}
