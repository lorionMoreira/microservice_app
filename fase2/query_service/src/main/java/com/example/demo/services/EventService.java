package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.repositories.PostRepository;
import com.example.demo.requests.EventData;

@Service
public class EventService {

    private final PostRepository postRepository;

    @Autowired
    public EventService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void createPost(EventData data) {
        Post post = new Post(data.getId(), data.getTitle());
        postRepository.save(post);
    }

    public void addCommentToPost(EventData data) {
        Post post = postRepository.findById(data.getPostId())
        .orElseThrow(() -> new RuntimeException("Post not found"));
        
        Comment comment = new Comment(data.getId(), data.getContent());
        post.getComments().add(comment);
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

}
