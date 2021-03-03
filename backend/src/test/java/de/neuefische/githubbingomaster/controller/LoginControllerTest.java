package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.LoginDto;
import de.neuefische.githubbingomaster.security.AppUser;
import de.neuefische.githubbingomaster.security.AppUserDb;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {


    @LocalServerPort
    private int port;

    private String getUrl() {
        return "http://localhost:" + port + "auth/login";
    }


    @Autowired
    private AppUserDb appUserDb;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loginWithValidCredentailsShouldGenerateJwtToken() {
        //GIVEN
        String username = "jan";
        String password = "super-secret";

        String encode = passwordEncoder.encode(password);
        appUserDb.save(new AppUser(username, encode));


        //WHEN
        ResponseEntity<String> response = restTemplate.postForEntity(getUrl(), new LoginDto(username, password), String.class);

        //THEN
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response.getBody(), Matchers.is("jwt token"));


    }

    @Test
    public void loginWithInValidCredentailsShouldGenerateJwtToken() {
        //GIVEN
        String username = "jan";
        String password = "super-secret";

        String encode = passwordEncoder.encode(password);
        appUserDb.save(new AppUser(username, encode));


        //WHEN
        ResponseEntity<String> response = restTemplate.postForEntity(getUrl(), new LoginDto(username, "worng paswword"), String.class);

        //THEN
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody(), Matchers.is("jwt token"));


    }

}
