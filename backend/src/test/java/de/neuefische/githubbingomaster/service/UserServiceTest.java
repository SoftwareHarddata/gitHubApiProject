package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.UserDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    public void testAddANewUser(){
        // GIVEN
        GitHubApiService gitHubApiService = mock(GitHubApiService.class);
        UserDb userDb = mock(UserDb.class);
        when(gitHubApiService.getUserprofile("mr-foobar"))
                .thenReturn(Optional.of(
                        GitHubProfile.builder()
                                .login("mr-foobar")
                                .avatarUrl("mr-foobars-avatar")
                                .build()));
        when(userDb.hasUser("mr-foobar")).thenReturn(false);
        when(userDb.addUser(
                User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build())
        ).thenReturn(User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build());
        UserService userService = new UserService(gitHubApiService, userDb);

        // WHEN
        User actual = userService.addUser("mr-foobar");

        // THEN
        assertThat(actual, is(User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build()));
        verify(userDb).addUser(User.builder().name("mr-foobar").avatar("mr-foobars-avatar").build());
    }

    @Test
    public void testAddANonGithubUser(){
        // GIVEN
        GitHubApiService gitHubApiService = mock(GitHubApiService.class);
        UserDb userDb = mock(UserDb.class);
        when(gitHubApiService.getUserprofile("mr-foobar"))
                .thenReturn(Optional.empty());

        UserService userService = new UserService(gitHubApiService, userDb);

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser("mr-foobar"));

        // THEN
        verify(userDb, never()).addUser(any());
        verify(userDb, never()).hasUser(any());
    }

    @Test
    public void testAddExistingUser(){
        // GIVEN
        GitHubApiService gitHubApiService = mock(GitHubApiService.class);
        UserDb userDb = mock(UserDb.class);
        when(gitHubApiService.getUserprofile("mr-foobar"))
                .thenReturn(Optional.of(
                        GitHubProfile.builder()
                                .login("mr-foobar")
                                .avatarUrl("mr-foobars-avatar")
                                .build()));
        when(userDb.hasUser("mr-foobar")).thenReturn(true);
        UserService userService = new UserService(gitHubApiService, userDb);

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser("mr-foobar"));

        // THEN
        verify(userDb, never()).addUser(any());
    }

}