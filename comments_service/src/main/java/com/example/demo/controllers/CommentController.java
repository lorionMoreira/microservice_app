package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Comment;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.EventCommentCreated;
import com.example.demo.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CommentController {
        private final CommentService commentService;
        private final ObjectMapper objectMapper;

    @Autowired
    public CommentController(ObjectMapper objectMapper,CommentService commentService) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }



    @GetMapping("api/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable String postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("api/posts/{postId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable String postId, @RequestBody CommentDTO commentDTO) {

        Comment comment = commentService.createComment(postId, commentDTO.getContent());
        
        commentService.RequestCreateComment(comment);

        return ResponseEntity.ok(comment);
    }

    // handling incoming events
    @PostMapping("/api/events")
    public ResponseEntity<?> handleEvent(@RequestBody Map<String, Object> body) {
        String type = (String) body.get("type");

        if ("CommentModerated".equals(type)) {
            EventCommentCreated eventCommentCreated = objectMapper.convertValue(body.get("data"), EventCommentCreated.class);
            // Assuming you have a method in your service to update the comment status
            Comment comment  = commentService.updateCommentStatus(eventCommentCreated.getPostId(), eventCommentCreated.getId(), eventCommentCreated.getStatus());

            commentService.RequestCommentUpdated(comment);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Unsupported event type");
    }
}
// criar controller de evento