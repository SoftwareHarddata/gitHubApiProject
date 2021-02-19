package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.db.UserDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.model.AddUserDto;
import de.neuefische.githubbingomaster.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private String getUrl(){
        return "http://localhost:"+port+"api/user";
    }

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserDb userDb;

    @BeforeEach
    public void setup(){
        userDb.clear();
    }

    @Test
    public void addNewUser(){
        // GIVEN
        AddUserDto userDto = AddUserDto.builder().name("mr-foobar").build();
        when(restTemplate.getForEntity("https://api.github.com/users/mr-foobar", GitHubProfile.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubProfile.builder().login("mr-foobar").avatarUrl("mr-foobars-avatar").build()));

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(),userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build()));
        assertTrue(userDb.hasUser("mr-foobar"));
    }

    @Test
    public void addExistingUser(){
        // GIVEN
        userDb.addUser(User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build());
        AddUserDto userDto = AddUserDto.builder().name("mr-foobar").build();
        when(restTemplate.getForEntity("https://api.github.com/users/mr-foobar", GitHubProfile.class))
                .thenReturn(ResponseEntity.ok(
                        GitHubProfile.builder().login("mr-foobar").avatarUrl("mr-foobars-avatar").build()));

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(),userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void addNonGitHubUser(){
        // GIVEN
        AddUserDto userDto = AddUserDto.builder().name("mr-foobar").build();
        when(restTemplate.getForEntity("https://api.github.com/users/mr-foobar", GitHubProfile.class))
                .thenThrow(RestClientException.class);

        // WHEN
        ResponseEntity<User> response = testRestTemplate.postForEntity(getUrl(),userDto, User.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}