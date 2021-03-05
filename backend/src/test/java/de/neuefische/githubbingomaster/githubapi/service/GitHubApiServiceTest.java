package de.neuefische.githubbingomaster.githubapi.service;

import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
                GitHubRepo.builder()
                        .id("123")
                        .repository("repo1")
                        .repositoryUrl("some-url-1")
                        .build(),
                GitHubRepo.builder()
                        .id("234")
                        .repository("repo2")
                        .repositoryUrl("some-url-2")
                        .build()
        };

        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo[].class)).thenReturn(new ResponseEntity<>(mockedRepos, HttpStatus.OK));

        // WHEN
        List<GitHubRepo> actual = gitHubApiService.getUserRepos(gitHubUser);

        // THEN
        assertThat(actual, is(List.of(
                GitHubRepo.builder()
                        .id("123")
                        .repository("repo1")
                        .repositoryUrl("some-url-1")
                        .build(),
                GitHubRepo.builder()
                        .id("234")
                        .repository("repo2")
                        .repositoryUrl("some-url-2")
                        .build()
        )));
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
        List<GitHubRepo> actual = gitHubApiService.getUserRepos(gitHubUser);

        // THEN
        assertThat(actual, is(new ArrayList<>()));

    }

    @Test
    @DisplayName("Get Repository should call githubapi and return repository")
    public void getRepositoryFromGithub(){
        //GIVEN
        String gitHubUser = "super-user";
        String gitHubRepo = "super-repo";
        String gitHubUrl = "https://api.github.com/repos/super-user/super-repo";
        GitHubRepo mockedRepo  =
                GitHubRepo.builder()
                        .id("234")
                        .repository("super-repo")
                        .repositoryUrl("some-url-2")
                        .build();


        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo.class)).thenReturn(new ResponseEntity<>(mockedRepo, HttpStatus.OK));
        //WHEN

        Optional<GitHubRepo> response = gitHubApiService.getRepository(gitHubUser, gitHubRepo);

        //THEN
        assertThat(response.isEmpty(), is(false));
        assertThat(response.get(), is(GitHubRepo.builder()
                .id("234")
                .repository("super-repo")
                .repositoryUrl("some-url-2")
                .build()));
    }


    @Test
    @DisplayName("Get Repository should call github api and return optional empty when repo is not available")
    public void getRepositoryFromGithubNotFound(){
        //GIVEN
        String gitHubUser = "super-user";
        String gitHubRepo = "super-repo";
        String gitHubUrl = "https://api.github.com/repos/super-user/super-repo";


        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo.class)).thenThrow(RestClientException.class);
        //WHEN

        Optional<GitHubRepo> response = gitHubApiService.getRepository(gitHubUser, gitHubRepo);

        //THEN
        assertThat(response.isEmpty(), is(true));

    }

}
