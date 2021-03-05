package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.AddRepositoryToWatchlistDto;
import de.neuefische.githubbingomaster.model.UserRepository;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import de.neuefische.githubbingomaster.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/watchlist")
public class WatchlistController {

    private final WatchListService watchListService;

    @Autowired
    public WatchlistController(WatchListService watchListService) {
        this.watchListService = watchListService;
    }

    @GetMapping
    public List<UserRepository> getWatchlistRepos(){
        return watchListService.getWatchlist();
    }

    @PostMapping
    public UserRepository postRepository(@RequestBody AddRepositoryToWatchlistDto repositoryToAdd){
        return watchListService.addToWatchlist(repositoryToAdd.getUsername(),repositoryToAdd.getRepositoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, repositoryToAdd + " already exists in watchlist database"));
    }

    @DeleteMapping("{id}")
    public void deleteRepository(@PathVariable String id){
        watchListService.deleteFromWatchlist(id);
    }
}
