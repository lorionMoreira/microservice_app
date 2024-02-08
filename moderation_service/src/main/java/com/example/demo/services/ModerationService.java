package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.EventCommentCreated;
import com.example.demo.request.Event;

@Service
public class ModerationService {

    
    private final WebClient webClient;

    @Autowired
    public ModerationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:4005").build();
    } 

    public String moderatingComment2(EventCommentCreated eventCommentCreated) {
        // TODO Auto-generated method stub
     String content = eventCommentCreated.getContent();

     String status;
     if (content.contains("orange")) {
         status = "rejected";
     } else {
         status = "approved";
     }

     return status;
        
    }

    public EventCommentCreated moderatingComment(EventCommentCreated eventCommentCreated) {
        // Simulate delay
        try {
            // Delay for a specific time (e.g., 5000 milliseconds or 5 seconds)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }

        String content = eventCommentCreated.getContent();

        String status;
        if (content.contains("orange")) {
            status = "rejected";
            eventCommentCreated.setStatus(status);
        } else {
            status = "approved";
            eventCommentCreated.setStatus(status);
        }

        return eventCommentCreated;
        
    }

    public void sendingCommentStatus(EventCommentCreated eventCommentCreated) {

        Event<EventCommentCreated> event = new Event<>("CommentModerated",eventCommentCreated);
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
        .subscribe(response -> {
            String responseBody = response.toString(); // Convert the response to a String
            System.out.println("Response Body: " + responseBody);
        });
    }
    
}
