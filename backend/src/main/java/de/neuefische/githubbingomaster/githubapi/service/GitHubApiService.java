package de.neuefische.githubbingomaster.githubapi.service;

import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class GitHubApiService {

    private String baseUrl = "https://api.github.com/users";

    private RestTemplate restTemplate;

    @Autowired
    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<GitHubProfile> getUserprofile(String loginName){
        String url = baseUrl + "/" + loginName;
        try{
            ResponseEntity<GitHubProfile> response = restTemplate.getForEntity(url, GitHubProfile.class);

            return Optional.of(response.getBody());
        }catch(RestClientException e){
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

}
