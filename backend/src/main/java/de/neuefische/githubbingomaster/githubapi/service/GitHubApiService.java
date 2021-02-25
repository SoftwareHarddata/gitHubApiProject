package de.neuefische.githubbingomaster.githubapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GitHubApiService {

    private String baseUrl = "https://api.github.com/users";

    private RestTemplate restTemplate;

    @Autowired
    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<GitHubProfile> getUserprofile(String loginName) {
        String url = baseUrl + "/" + loginName;
        try {
            ResponseEntity<GitHubProfile> response = restTemplate.getForEntity(url, GitHubProfile.class);

            return Optional.of(response.getBody());
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    public List<GitHubRepo> getUserRepos(String name) {
        String url = baseUrl + "/" + name + "/repos";

        try {
            ResponseEntity<GitHubRepo[]> response = restTemplate.getForEntity(url, GitHubRepo[].class);
            return List.of(response.getBody());
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

}
