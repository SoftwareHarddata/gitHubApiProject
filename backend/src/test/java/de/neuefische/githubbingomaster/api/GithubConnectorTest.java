package de.neuefische.githubbingomaster.api;

import de.neuefische.githubbingomaster.model.GithubUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GithubConnectorTest {

    public final String url = "https://api.github.com";


    @Test
    public void getUserShouldReturnUser() {
        //Given
        String name = "magnus";
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        GithubConnector githubConnector = new GithubConnector(mockRestTemplate);
        GithubUser mockedUser = new GithubUser("magnus", "http://awesomepics.com/magnus");

        when(mockRestTemplate.getForEntity(url + "/users/" + name, GithubUser.class)).thenReturn(new ResponseEntity<>(mockedUser, HttpStatus.OK));

        //When
        Optional<GithubUser> testUser = githubConnector.getUser(name);

        // Then
        assertThat(testUser.get(), is(new GithubUser("magnus", "http://awesomepics.com/magnus")));

    }

    @Test
    public void getUserShouldReturnEmptyOptionalForNonExistingUser(){
        // Given
        String name = "magnus";
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        GithubConnector githubConnector = new GithubConnector(mockRestTemplate);

        when(mockRestTemplate.getForEntity(url + "/users/" + name, GithubUser.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        //When
        Optional<GithubUser> testUser = githubConnector.getUser(name);

        //Then
        assertTrue(testUser.isEmpty());

    }

}