package com.example.demo.request;
import com.example.demo.request.EventData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String type;
    private EventData data;

    // Constructors, getters, and setters...
}
