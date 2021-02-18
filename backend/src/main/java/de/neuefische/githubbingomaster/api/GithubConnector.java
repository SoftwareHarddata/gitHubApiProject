package de.neuefische.githubbingomaster.api;

import de.neuefische.githubbingomaster.model.GithubUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class GithubConnector {

    private final String apiEndpoint = "https://api.github.com";
    private RestTemplate restTemplate;

    public Optional<GithubUser> getUser(String name) {

        ResponseEntity<GithubUser> response = restTemplate.getForEntity(apiEndpoint + "/users/" + name, GithubUser.class);

        if(response.getStatusCode().equals(HttpStatus.OK)){
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }

}
