package com.example.demo.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventData {
    private String id;
    private String title; // Used for PostCreated
    private String content; // Used for CommentCreated
    private String postId;
}
