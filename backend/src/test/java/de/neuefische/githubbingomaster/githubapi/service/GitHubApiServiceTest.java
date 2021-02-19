package de.neuefische.githubbingomaster.githubapi.service;

import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GitHubApiServiceTest {

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private GitHubApiService gitHubApiService = new GitHubApiService(restTemplate);

    @Test
    @DisplayName("returns a non empty optional of an existing github user")
    public void getsAGitHubProfile(){
        // GIVEN
        String gitHubUser = "super-user";
        GitHubProfile profile = GitHubProfile.builder().login(gitHubUser).avatarUrl("https://avatars.githubusercontent.com/u/48794499?v=4").build();
        ResponseEntity<GitHubProfile> response = ResponseEntity.ok(profile);
        when(restTemplate.getForEntity("https://api.github.com/users/"+gitHubUser, GitHubProfile.class)).thenReturn(response);

        // WHEN
        Optional<GitHubProfile> actual = gitHubApiService.getUserprofile(gitHubUser);

        // THEN
        assertThat(actual.get(), is(GitHubProfile.builder().login(gitHubUser).avatarUrl("").build()));
    }

    @Test
    @DisplayName("returns a empty optional of an non existing github user")
    public void getNonExistingUser(){
        // GIVEN
        String gitHubUser = "no-a-user";
        when(restTemplate.getForEntity("https://api.github.com/users/"+gitHubUser, GitHubProfile.class))
                .thenThrow(RestClientException.class);

        // WHEN
        Optional<GitHubProfile> actual = gitHubApiService.getUserprofile(gitHubUser);

        // THEN
        assertTrue(actual.isEmpty());
    }

}