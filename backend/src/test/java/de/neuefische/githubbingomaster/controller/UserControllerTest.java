package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.db.UserMongoDb;
import de.neuefische.githubbingomaster.db.WatchlistMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.model.AddUserDto;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private String getUrl() {
        return "http://localhost:" + port + "api/user";
    }

    private String getRepoUrl(String userName) {
        return  getUrl() + "/" + userName + "/repos";
    }

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserMongoDb userDb;

    @Autowired
    private WatchlistMongoDb watchlistMongoDb;

    @BeforeEach
    public void setup() {
        watchlistMongoDb.deleteAll();
        userDb.deleteAll();
    }

    @Test
    @DisplayName("Adding a new user via its github login adds the user to the database")
    public void addNewUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String avatarUrl = "mr-foobars-avatar";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        AddUserDto userDto = AddUserDto.builder().name(gitHubUser).build();
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubProfile.builder().login(gitHubUser).avatarUrl(avatarUrl).build()));

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(User.builder().name(gitHubUser).avatar(avatarUrl).build()));
        assertTrue(userDb.existsById(gitHubUser));
    }

    @Test
    @DisplayName("Adding a user twice results in 400 (Bad Request)")
    public void addExistingUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String avatarUrl = "mr-foobars-avatar";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        userDb.save(User.builder().name(gitHubUser).avatar(avatarUrl).build());
        AddUserDto userDto = AddUserDto.builder().name(gitHubUser).build();
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubProfile.builder().login(gitHubUser).avatarUrl(avatarUrl).build()));

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Adding a user who is not a github user results in 400 (Bad Request)")
    public void addNonGitHubUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        AddUserDto userDto = AddUserDto.builder().name(gitHubUser).build();
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class))
                .thenThrow(RestClientException.class);

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Get user should return a list of all users")
    public void getAllUsers() {
        //GIVEN
        userDb.save(new User("supergithubuser", "someavatar"));
        userDb.save(new User("secondUser", "someOtheravatar"));

        //WHEN
        ResponseEntity<User[]> response = testRestTemplate.getForEntity(getUrl(), User[].class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(
                new User("supergithubuser", "someavatar"),
                new User("secondUser", "someOtheravatar")));
    }

    @Test
    @DisplayName("Get user by username should return user")
    public void getUser() {
        //GIVEN
        userDb.save(new User("supergithubuser", "someavatar"));
        userDb.save(new User("secondUser", "someOtheravatar"));
        //WHEN
        ResponseEntity<User> response = testRestTemplate.getForEntity(getUrl() + "/secondUser", User.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new User("secondUser", "someOtheravatar")));
    }

    @Test
    @DisplayName("Get user by username should return not found 404 when user not exists")
    public void getUserNotFound() {
        //GIVEN
        userDb.save(new User("supergithubuser", "someavatar"));
        userDb.save(new User("secondUser", "someOtheravatar"));
        //WHEN
        ResponseEntity<Void> response = testRestTemplate.getForEntity(getUrl() + "/unknownUser", Void.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Get repositories returns repositories")
    public void getRepositoriesReturnsRepositories() {
        //Given
        String gitHubUrl = "https://api.github.com/users/supergithubuser/repos";
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
        userDb.save(new User("supergithubuser", "someavatar"));


        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo[].class))
                .thenReturn(new ResponseEntity<>(mockedRepos, HttpStatus.OK));

        // When
        ResponseEntity<UserRepository[]> response = testRestTemplate.getForEntity(getUrl() + "/supergithubuser/repos", UserRepository[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new UserRepository[]{
                UserRepository.builder()
                        .id("123")
                        .repositoryName("repo1")
                        .repositoryWebUrl("some-url-1")
                        .build(),
                UserRepository.builder()
                        .id("234")
                        .repositoryName("repo2")
                        .repositoryWebUrl("some-url-2")
                        .build()
        }));
    }

    @Test
    @DisplayName("Get repositories should throw error for non existing user")
    public void getRepositoriesShouldThrowExceptionForNonExistingUser() {
        // When
        ResponseEntity<Void> response = testRestTemplate.getForEntity(getUrl() + "/supergithubuser/repos", Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Post New Repository to Watchlist for existing User")
    public void postNewRepositoryForWatchlist() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String repositoryId = "1";
        String avatarUrl = "mr-foobars-avatar";
        String repository = "hello-world";
        String repositoryUrl = "hello-world.de";

        User user = User.builder()
                .name(gitHubUser)
                .avatar(avatarUrl)
                .build();

        userDb.save(user);

        UserRepository userRepository = UserRepository.builder()
                .id(repositoryId)
                .repositoryName(repository)
                .repositoryWebUrl(repositoryUrl)
                .onWatchlist(false)
                .build();

        UserRepository expectedRepository = UserRepository.builder()
                .id(repositoryId)
                .repositoryName(repository)
                .repositoryWebUrl(repositoryUrl)
                .onWatchlist(true)
                .build();

        // WHEN
        ResponseEntity<UserRepository> response =
                testRestTemplate.postForEntity(getRepoUrl(gitHubUser), userRepository, UserRepository.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedRepository));
        assertTrue(watchlistMongoDb.existsById(repositoryId));
    }
}
