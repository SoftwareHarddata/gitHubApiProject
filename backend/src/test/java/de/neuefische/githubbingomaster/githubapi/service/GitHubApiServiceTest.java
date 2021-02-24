package de.neuefische.githubbingomaster.githubapi.service;

import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GitHubApiServiceTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);

    private final GitHubApiService gitHubApiService = new GitHubApiService(restTemplate);

    @Test
    @DisplayName("Returns profile of a github user")
    public void getsAGitHubProfile() {
        // GIVEN
        String gitHubUser = "super-user";
        String avatarUrl = "https://avatars.githubusercontent.com/u/48794499?v=4";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        GitHubProfile profile = GitHubProfile.builder().login(gitHubUser).avatarUrl(avatarUrl).build();
        ResponseEntity<GitHubProfile> response = ResponseEntity.ok(profile);
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class)).thenReturn(response);

        // WHEN
        Optional<GitHubProfile> actual = gitHubApiService.getUserprofile(gitHubUser);

        // THEN
        assertThat(actual.get(), is(GitHubProfile.builder().login(gitHubUser).avatarUrl(avatarUrl).build()));
    }

    @Test
    @DisplayName("Returns an empty optional when the user is not a github user")
    public void getNonExistingUser() {
        // GIVEN
        String gitHubUser = "no-a-user";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class))
                .thenThrow(RestClientException.class);

        // WHEN
        Optional<GitHubProfile> actual = gitHubApiService.getUserprofile(gitHubUser);

        // THEN
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Returns repos of a github user")
    public void getsGitHubRepos() {
        // GIVEN
        String gitHubUser = "super-user";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser + "/repos";
        GitHubRepo[] mockedRepos = {
                new GitHubRepo("repo1", "some-url-1"),
                new GitHubRepo("repo2", "some-url-2")

        };

        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo[].class)).thenReturn(new ResponseEntity<>(mockedRepos, HttpStatus.OK));

        // WHEN
        GitHubRepo[] actual = gitHubApiService.getUserRepos(gitHubUser);

        // THEN
        assertThat(actual, is(new GitHubRepo[]{
                new GitHubRepo("repo1", "some-url-1"),
                new GitHubRepo("repo2", "some-url-2")
        }));
    }

    @Test
    @DisplayName("Returns an empty array when user does not exist")
    public void getReposFromNonExistingUser() {
        // GIVEN
        String gitHubUser = "not-a-user";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser + "/repos";
        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo[].class))
                .thenThrow(RestClientException.class);

        // WHEN
        GitHubRepo[] actual = gitHubApiService.getUserRepos(gitHubUser);

        // THEN
        assertThat(actual, is(new GitHubRepo[]{}));

    }

}
