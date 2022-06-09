package com.zyh.service;

import com.zyh.entity.Mention;
import com.zyh.entity.Post;

import java.util.List;
import java.util.Set;

public interface MentionService {
    void addMention(Post post, Set<String> newSet);

    List<Mention> findMentionsByUid(Integer id);

    Mention findMentionById(Integer id);

    boolean deleteMention(Mention mention);


}
