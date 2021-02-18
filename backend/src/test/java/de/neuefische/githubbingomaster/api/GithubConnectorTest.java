package de.neuefische.githubbingomaster.api;

import de.neuefische.githubbingomaster.model.GithubUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GithubConnectorTest {

    public final String url = "http://api.test.com";


    @Test
    public void getUserShouldReturnUser() {
        //Given
        String name = "magnus";
        TestRestTemplate mockRestTemplate = mock(TestRestTemplate.class);
        GithubConnector githubConnector = new GithubConnector(url, mockRestTemplate);

        when(githubConnector.getUser()).then(new GithubUser("magnus", "http://awesomepics.com/magnus"))

        //

    }

}