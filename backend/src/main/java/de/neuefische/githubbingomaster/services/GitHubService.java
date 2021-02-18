package de.neuefische.githubbingomaster.services;

import de.neuefische.githubbingomaster.model.GitHubUser;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GitHubService {
    String baseURL = "https://api.github.com/users/";
    private final RestTemplate restTemplate;

    //
    @Autowired
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<GitHubUser> getUser(String login) {
        try {
            ResponseEntity<GitHubUser> newUser = restTemplate.getForEntity(baseURL + login, GitHubUser.class);
            if (newUser.hasBody() && newUser.getBody() != null) {
                return Optional.of(newUser.getBody());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return Optional.empty();
    }
}
