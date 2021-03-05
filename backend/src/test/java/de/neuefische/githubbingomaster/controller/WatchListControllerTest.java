package de.neuefische.githubbingomaster.controller;

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
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WatchListControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WatchlistMongoDb watchlistMongoDb;

    @Autowired
    private AppUserDb appUserDb;


    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void setup() {
        watchlistMongoDb.deleteAll();
        appUserDb.deleteAll();
    }

    private String getUrl() {
        return "http://localhost:" + port + "api/watchlist";
    }


    @Test
    @DisplayName("Post New Repository to Watchlist")
    public void postNewRepositoryForWatchlist() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String githubRepositoryName = "repo-name";
        String repositoryId = "1";
        String repositoryUrl = "hello-world.de";

        String gitHubUrl = "https://api.github.com/repos/" + gitHubUser + "/" + githubRepositoryName;

        when(restTemplate.getForEntity(gitHubUrl, GitHubRepo.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubRepo.builder().id(repositoryId)
                                .repository(githubRepositoryName)
                                .repositoryUrl(repositoryUrl).build()));

        // WHEN
        AddRepositoryToWatchlistDto request = new AddRepositoryToWatchlistDto(gitHubUser, githubRepositoryName);

        HttpHeaders headers = new HttpHeaders();
        String token = loginToApp();
        headers.setBearerAuth(token);
        HttpEntity<AddRepositoryToWatchlistDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UserRepository> response = testRestTemplate.postForEntity(getUrl(), entity, UserRepository.class);

        // THEN
        UserRepository expectedRepository = UserRepository.builder()
                .id(repositoryId)
                .repositoryName(githubRepositoryName)
                .repositoryWebUrl(repositoryUrl)
                .onWatchlist(true)
                .build();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedRepository));
        assertTrue(watchlistMongoDb.existsById(repositoryId));
    }


    @Test
    @DisplayName("Get Repositories on watchlist")
    public void getRepositoriesOnWatchlist() {
        // GIVEN
        watchlistMongoDb.save(WatchlistRepository.builder()
                .id("123")
                .repositoryName("repo-name")
                .repositoryWebUrl("repo-url")
                .build());
        watchlistMongoDb.save(WatchlistRepository.builder()
                .id("222")
                .repositoryName("repo-name2")
                .repositoryWebUrl("repo-url2")
                .build());


        // WHEN
        HttpHeaders headers = new HttpHeaders();
        String token = loginToApp();
        headers.setBearerAuth(token);
        HttpEntity<AddRepositoryToWatchlistDto> entity = new HttpEntity<>(headers);

        ResponseEntity<UserRepository[]> response = testRestTemplate.exchange(getUrl(), HttpMethod.GET, entity, UserRepository[].class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(
                UserRepository.builder()
                        .id("123")
                        .repositoryName("repo-name")
                        .repositoryWebUrl("repo-url")
                        .onWatchlist(true)
                        .build(),
                UserRepository.builder()
                        .id("222")
                        .repositoryName("repo-name2")
                        .repositoryWebUrl("repo-url2")
                        .onWatchlist(true)
                        .build()
        ));

    }


    @Test
    @DisplayName("Delete Repository from watchlist")
    public void deleteRepositoryFromWatchlist() {
        // GIVEN

        watchlistMongoDb.save(WatchlistRepository.builder()
                .id("222")
                .repositoryName("repo-name2")
                .repositoryWebUrl("repo-url2")
                .build());


        // WHEN
        HttpHeaders headers = new HttpHeaders();
        String token = loginToApp();
        headers.setBearerAuth(token);
        HttpEntity<AddRepositoryToWatchlistDto> entity = new HttpEntity<>(headers);

        ResponseEntity<UserRepository[]> response = testRestTemplate.exchange(getUrl()+"/222", HttpMethod.DELETE, entity, UserRepository[].class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(watchlistMongoDb.existsById("222"),is(false));

    }



    private String loginToApp() {
        String password = encoder.encode("superSecretPassword");
        appUserDb.save(new AppUser("jan", password));
        ResponseEntity<String> loginResponse = testRestTemplate.postForEntity("http://localhost:" + port + "auth/login", new LoginDto("jan", "superSecretPassword"), String.class);
        return loginResponse.getBody();
    }
}
