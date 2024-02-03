package com.example.demo.services;

import com.example.demo.domain.Post;
import com.example.demo.dto.PostDTO;
import com.example.demo.repositories.PostRepository;
import com.example.demo.request.Event;
import com.example.demo.request.EventData;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final WebClient webClient;

    @Autowired
    public PostService(WebClient.Builder webClientBuilder,PostRepository postRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:4005").build();
        this.postRepository = postRepository;
    }

    public Post createPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void makingRequestPost(Post post) {
        EventData eventData = new EventData(post.getId(), post.getTitle());
        Event event = new Event("PostCreated", eventData);

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

    // Event and EventData classes

}
