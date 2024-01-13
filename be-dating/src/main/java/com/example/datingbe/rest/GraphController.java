package com.example.datingbe.rest;

import com.example.datingbe.entity.User;
import com.example.datingbe.service.GraphService;
import com.example.datingbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;
    private final UserRepository userRepository;

    @Autowired
    public GraphController(GraphService graphService, UserRepository userRepository) {
        this.graphService = graphService;
        this.userRepository = userRepository;
    }

    @GetMapping("/relatedUsers/{userId}")
    public List<User> getTopRelatedUsers(@PathVariable Long userId, @RequestParam(defaultValue = "10") int topCount) {
        User startingUser = userRepository.findById(userId).orElse(null);
        if (startingUser == null) {
            // Handle user not found
            return null;
        }
        graphService.buildGraphUsers();

       return graphService.buildPendingRequestGraphUsers(startingUser);
    }
}