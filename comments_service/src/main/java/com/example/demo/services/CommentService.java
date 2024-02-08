package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.domain.Comment;
import com.example.demo.dto.EventCommentCreated;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.request.Event;

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

        comment.setStatus("pending");
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    public void RequestCreateComment(Comment comment) {

      EventCommentCreated eventCommentCreated = new EventCommentCreated(comment.getId(),comment.getContent(),comment.getPostId(),comment.getStatus());
      Event<EventCommentCreated> event = new Event<>("CommentCreated",eventCommentCreated);

      sendEvent(event);
        // Send the event to the other service

    }

    @Transactional
    public Comment updateCommentStatus(String postId, String commentId, String status) {
        // Assuming postId is also necessary for the update logic
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            // Here, you could add an additional check to make sure the postId matches if necessary
            comment.setStatus(status);
            commentRepository.save(comment);

            return comment;
        } else {
            // Handle the case where the comment does not exist
            // This could involve throwing an exception or any other business logic
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
    }

    public void RequestCommentUpdated(Comment comment) {

        EventCommentCreated eventCommentCreated = new EventCommentCreated(comment.getId(),comment.getContent(),comment.getPostId(),comment.getStatus());
        Event<EventCommentCreated> event = new Event<>("CommentUpdated",eventCommentCreated);
  
        sendEvent(event);
          // Send the event to the other service
  
      }

    private void sendEvent(Event<?> event) {
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
