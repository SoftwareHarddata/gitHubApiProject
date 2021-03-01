package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.model.AddUserDto;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.UserRepository;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import de.neuefische.githubbingomaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody AddUserDto dto) {
        return this.userService.addUser(dto.getName());
    }

    @GetMapping
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @GetMapping("{username}/repos")
    public List<UserRepository> getRepositories(@PathVariable String username) {
        return userService.getRepositories(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, username + " does not exist in database"));
    }

    @GetMapping("watchlist")
    public List<WatchlistRepository> getWatchlistRepos(){
        return userService.getWatchlist();
    }

    @PostMapping("{username}/repos")
    public UserRepository postRepository(@RequestBody UserRepository userRepository, @PathVariable String username){
        return userService.addToWatchlist(userRepository, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, userRepository + " already exists in watchlist database"));
    }

    @DeleteMapping("{username}/repos")
    public UserRepository deleteRepository(@RequestBody UserRepository userRepository){
        return userService.deleteFromWatchlist(userRepository);
    }
}
