package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.EventCommentCreated;
import com.example.demo.dto.EventPostCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.example.demo.domain.Query;

import com.example.demo.services.QueryService;

@RestController
public class QueryController {
    private final QueryService eventService;
    private final ObjectMapper objectMapper;

    @Autowired
    public QueryController(ObjectMapper objectMapper,QueryService eventService) {
        this.objectMapper = objectMapper;
        this.eventService = eventService;
    }

    @GetMapping("/api/getQuery")
    public ResponseEntity<List<Query>> getAllPosts() {
        List<Query> posts = eventService.getAllPosts();
        return ResponseEntity.ok(posts);
    }


    // handling incoming events
    @PostMapping("/api/events")
    public ResponseEntity<?> handleEvent(@RequestBody Map<String, Object> body) {
        String type = (String) body.get("type");
        if ("PostCreated".equals(type)) {
            EventPostCreated eventPostCreated = objectMapper.convertValue(body.get("data"), EventPostCreated.class);
            eventService.createPost(eventPostCreated);
            
        } else if ("CommentCreated".equals(type)) {
            EventCommentCreated eventCommentCreated = objectMapper.convertValue(body.get("data"), EventCommentCreated.class);
            eventService.addCommentToPost(eventCommentCreated);

        } else if ("CommentUpdated".equals(type)) {
            EventCommentCreated eventCommentCreated = objectMapper.convertValue(body.get("data"), EventCommentCreated.class);
            eventService.updatingCommentToPost(eventCommentCreated);
        }

        return ResponseEntity.ok().build();
    }


}
