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

    private final GitHubApiService gitHubApiService = mock(GitHubApiService.class);
    private final UserDb userDb = mock(UserDb.class);
    private final UserService userService = new UserService(gitHubApiService, userDb);

    @Test
    public void testAddANewUser(){
        // GIVEN
        String gitHubUser = "mr-foobar";
        String avatarUrl = "mr-foobars-avatar";
        GitHubProfile gitHubProfile = GitHubProfile.builder()
                .login(gitHubUser)
                .avatarUrl(avatarUrl)
                .build();

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.of(gitHubProfile));

        when(userDb.hasUser(gitHubUser)).thenReturn(false);

        User mockUser = User.builder().name(gitHubUser).avatar(avatarUrl).build();
        when(userDb.addUser(mockUser))
                .thenReturn(mockUser);

        // WHEN
        User actual = userService.addUser(gitHubUser);

        // THEN
        User expectedUser = User.builder().name(gitHubUser).avatar(avatarUrl).build();
        assertThat(actual, is(expectedUser));
        verify(userDb).addUser(expectedUser);
    }

    @Test
    public void testAddANonGithubUser(){
        // GIVEN
        String gitHubUser = "mr-foobar";

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.empty());

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser(gitHubUser));

        // THEN
        verify(userDb, never()).addUser(any());
        verify(userDb, never()).hasUser(any());
    }

    @Test
    public void testAddExistingUser(){
        // GIVEN
        String gitHubUser = "mr-foobar";
        GitHubProfile gitHubProfile = GitHubProfile.builder()
                .login(gitHubUser)
                .avatarUrl("mr-foobars-avatar")
                .build();

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.of(gitHubProfile));

        when(userDb.hasUser(gitHubUser)).thenReturn(true);

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser(gitHubUser));

        // THEN
        verify(userDb, never()).addUser(any());
    }

}