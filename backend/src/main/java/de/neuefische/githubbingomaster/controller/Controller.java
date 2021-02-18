package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.GitHubUser;
import de.neuefische.githubbingomaster.model.RequestUser;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    private final UserService userService;

    @Autowired
    public Controller(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<GitHubUser> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/user/{login}")
    public GitHubUser getUser(@PathVariable String login){
        return userService.getUser(login).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @PostMapping
    public GitHubUser addUser(@RequestBody RequestUser requestUser){
        return userService.addUser(requestUser).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }
}
