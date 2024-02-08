package com.example.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EventCommentCreated;
import com.example.demo.services.ModerationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ModerationController {
    private final ModerationService moderationService;
    private final ObjectMapper objectMapper;

    
    public ModerationController(ModerationService moderationService, ObjectMapper objectMapper) {
        this.moderationService = moderationService;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/api/events")
    public ResponseEntity<?> handleEvent(@RequestBody Map<String, Object> body) {
        String type = (String) body.get("type");
         if ("CommentCreated".equals(type)) {

            EventCommentCreated eventCommentCreated = objectMapper.convertValue(body.get("data"), EventCommentCreated.class);
            EventCommentCreated eventCommentCreatedModerated = moderationService.moderatingComment(eventCommentCreated);
        
            moderationService.sendingCommentStatus(eventCommentCreatedModerated);

        } 

        return ResponseEntity.ok().build();
    }
}
