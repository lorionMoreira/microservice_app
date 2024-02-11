package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.EventCommentCreated;
import com.example.demo.dto.EventPostCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Query;
import com.example.demo.repositories.QueryRepository;

@Service
public class QueryService {

    private final QueryRepository queryRepository;

    @Autowired
    public QueryService(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Query> getAllPosts() {
        return queryRepository.findAll();
    }

    public void createPost(EventPostCreated data) {

        Query post = new Query(data.getId(), data.getTitle());
        queryRepository.save(post);
    }

    public void addCommentToPost(EventCommentCreated data) {

        Query query = queryRepository.findById(data.getPostId())
        .orElseThrow(() -> new RuntimeException("Post not found"));
        
        Comment comment = new Comment(data.getId(), data.getContent(),data.getStatus());
        query.getComments().add(comment);
        queryRepository.save(query);
    }



    public void updatingCommentToPost(EventCommentCreated data) {
        // TODO Auto-generated method stub
        Query query = queryRepository.findById(data.getPostId())
        .orElseThrow(() -> new RuntimeException("Post not found"));

        query.getComments().stream()
            .filter(comment -> comment.getId().equals(data.getId()))
            .findFirst()
            .ifPresent(comment -> {
                comment.setStatus(data.getStatus());
                comment.setContent(data.getContent());
            });

        if (query.getComments().stream().noneMatch(comment -> comment.getId().equals(data.getId()))) {
            throw new RuntimeException("Comment not found");
        }

        // Save the updated Query object back to the database
        queryRepository.save(query);

       // Update the comment in the list


    }

}
