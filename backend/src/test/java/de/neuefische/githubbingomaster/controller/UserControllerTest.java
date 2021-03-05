package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.db.UserMongoDb;
import de.neuefische.githubbingomaster.db.WatchlistMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.model.*;
import de.neuefische.githubbingomaster.security.AppUser;
import de.neuefische.githubbingomaster.security.AppUserDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserMongoDb userDb;

    @Autowired
    private WatchlistMongoDb watchlistMongoDb;

    @Autowired
    private AppUserDb appUserDb;


    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void setup() {
        watchlistMongoDb.deleteAll();
        userDb.deleteAll();
        appUserDb.deleteAll();
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
        String jwtToken = loginToApp();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<AddUserDto> entity = new HttpEntity<>(userDto, headers);
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), entity, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(User.builder().name(gitHubUser).avatar(avatarUrl).build()));
        assertTrue(userDb.existsById(gitHubUser));
    }

    @Test
    @DisplayName("Adding a new user ist forbidden when user has not valid token")
    public void addNewUserForbidden(){
        // GIVEN
        String gitHubUser = "mr-foobar";
        String avatarUrl = "mr-foobars-avatar";
        String gitHubUrl = "https://api.github.com/users/" + gitHubUser;
        AddUserDto userDto = AddUserDto.builder().name(gitHubUser).build();
        when(restTemplate.getForEntity(gitHubUrl, GitHubProfile.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubProfile.builder().login(gitHubUser).avatarUrl(avatarUrl).build()));

        //WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), userDto, User.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
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
        String jwtToken = loginToApp();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<AddUserDto> entity = new HttpEntity<>(userDto, headers);
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), entity, User.class);

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
        String jwtToken = loginToApp();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<AddUserDto> entity = new HttpEntity<>(userDto, headers);
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(), entity, User.class);

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
        String jwtToken = loginToApp();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<User[]> response = testRestTemplate.exchange(getUrl(), HttpMethod.GET, entity, User[].class);

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
        String jwtToken = loginToApp();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<User> response = testRestTemplate.exchange(getUrl() + "/secondUser", HttpMethod.GET, entity, User.class);

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
        String jwtToken = loginToApp();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(getUrl() + "/unknownUser", HttpMethod.GET, entity, Void.class);

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
        watchlistMongoDb.save(WatchlistRepository.builder()
                .id("123")
                .repositoryName("repo1")
                .repositoryWebUrl("some-url-1")
                .build());
        userDb.save(new User("supergithubuser", "someavatar"));


        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo[].class))
                .thenReturn(new ResponseEntity<>(mockedRepos, HttpStatus.OK));

        // When
        String jwtToken = loginToApp();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserRepository[]> response = testRestTemplate.exchange(getUrl() + "/supergithubuser/repos", HttpMethod.GET, entity, UserRepository[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new UserRepository[]{
                UserRepository.builder()
                        .id("123")
                        .repositoryName("repo1")
                        .repositoryWebUrl("some-url-1")
                        .onWatchlist(true)
                        .build(),
                UserRepository.builder()
                        .id("234")
                        .repositoryName("repo2")
                        .repositoryWebUrl("some-url-2")
                        .onWatchlist(false)
                        .build()
        }));
    }

    @Test
    @DisplayName("Get repositories should throw error for non existing user")
    public void getRepositoriesShouldThrowExceptionForNonExistingUser() {
        // When
        String jwtToken = loginToApp();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(getUrl() + "/supergithubuser/repos", HttpMethod.GET, entity, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    private String loginToApp() {
        String password = encoder.encode("superSecretPassword");
        appUserDb.save(new AppUser("jan", password));
        ResponseEntity<String> loginResponse = testRestTemplate.postForEntity("http://localhost:" + port + "auth/login", new LoginDto("jan", "superSecretPassword"), String.class);
        return loginResponse.getBody();
    }

}
