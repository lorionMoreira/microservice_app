package com.example.demo.request;

import com.example.demo.dto.EventCommentCreated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {
    private String type;
    private T data;
}
