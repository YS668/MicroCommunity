package com.MC.service;

import com.MC.entity.Mention;
import com.MC.entity.Post;

import java.util.List;
import java.util.Set;

public interface MentionService {
    void addMention(Post post, Set<String> newSet);

    List<Mention> findMentionsByUid(Integer id);

    Mention findMentionById(Integer id);

    boolean deleteMention(Mention mention);


}
