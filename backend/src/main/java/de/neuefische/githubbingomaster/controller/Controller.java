package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    private final GitHubService gitHubService;

    @Autowired
    public Controller(GitHubService gitHubService){
        this.gitHubService=gitHubService;
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return gitHubService.getUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        return gitHubService.getUser(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        return gitHubService.addUser(user);
    }





}
