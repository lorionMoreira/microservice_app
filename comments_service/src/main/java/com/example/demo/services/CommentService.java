package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.domain.Comment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.request.Event;
import com.example.demo.request.EventData;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebClient webClient;

    @Autowired
    public CommentService(WebClient.Builder webClientBuilder,CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:4005").build();
    } 

        public Comment createComment(String postId, String content) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    public void makingRequestPost(Comment comment) {
      EventData eventData = new EventData(comment.getId(),comment.getContent(),comment.getPostId());
      Event event = new Event("CommentCreated",eventData);


              // Send the event to the other service
              webClient.post()
              .uri("/events")
              .bodyValue(event)
              .retrieve()
              .bodyToMono(Void.class)
              .doOnError(error -> {
                  // handle error scenario here if needed
              })
              .subscribe(); // Asynchronously sends the request
        
    }
}
