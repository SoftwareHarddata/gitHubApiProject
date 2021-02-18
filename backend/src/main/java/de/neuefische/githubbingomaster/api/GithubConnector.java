package de.neuefische.githubbingomaster.api;

import de.neuefische.githubbingomaster.model.GithubUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Data
@AllArgsConstructor
public class GithubConnector {

    private final String apiEndpoint = "https://api.github.com";
    private RestTemplate restTemplate = new RestTemplate();

    public GithubUser getUser(String name) {
        return null;
    }

}
