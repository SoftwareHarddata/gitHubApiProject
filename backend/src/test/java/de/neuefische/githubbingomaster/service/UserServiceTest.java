package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.UserMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final GitHubApiService gitHubApiService = mock(GitHubApiService.class);
    private final UserMongoDb userDb = mock(UserMongoDb.class);
    private final UserService userService = new UserService(gitHubApiService, userDb);

    @Test
    @DisplayName("A new user whose name is a github login is added")
    public void testAddANewUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        String avatarUrl = "mr-foobars-avatar";
        GitHubProfile gitHubProfile = GitHubProfile.builder()
                .login(gitHubUser)
                .avatarUrl(avatarUrl)
                .build();

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.of(gitHubProfile));

        when(userDb.existsById(gitHubUser)).thenReturn(false);

        User mockUser = User.builder().name(gitHubUser).avatar(avatarUrl).build();
        when(userDb.save(mockUser))
                .thenReturn(mockUser);

        // WHEN
        User actual = userService.addUser(gitHubUser);

        // THEN
        User expectedUser = User.builder().name(gitHubUser).avatar(avatarUrl).build();
        assertThat(actual, is(expectedUser));
        verify(userDb).save(expectedUser);
    }

    @Test
    @DisplayName("A new user whose name is not a github login is not added")
    public void testAddANonGithubUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.empty());

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser(gitHubUser));

        // THEN
        verify(userDb, never()).save(any());
        verify(userDb, never()).existsById(any());
    }

    @Test
    @DisplayName("An already added user is not added again")
    public void testAddExistingUser() {
        // GIVEN
        String gitHubUser = "mr-foobar";
        GitHubProfile gitHubProfile = GitHubProfile.builder()
                .login(gitHubUser)
                .avatarUrl("mr-foobars-avatar")
                .build();

        when(gitHubApiService.getUserprofile(gitHubUser))
                .thenReturn(Optional.of(gitHubProfile));

        when(userDb.existsById(gitHubUser)).thenReturn(true);

        // WHEN
        assertThrows(ResponseStatusException.class, () -> userService.addUser(gitHubUser));

        // THEN
        verify(userDb, never()).save(any());
    }

    @Test
    @DisplayName("List users should return list from db")
    public void listUsers() {
        //GIVEN
        when(userDb.findAll()).thenReturn(List.of(
                new User("supergithubuser", "someavatar"),
                new User("secondUser", "someOtheravatar")));
        //WHEN
        List<User> users = userService.listUsers();

        //THEN
        assertThat(users, containsInAnyOrder(
                new User("supergithubuser", "someavatar"),
                new User("secondUser", "someOtheravatar")));
    }

    @Test
    @DisplayName("getUserByUsername should return existing user from Db")
    public void getExistingUser() {
        //GIVEN
        String username = "existingUserName";
        when(userDb.findById(username)).thenReturn(Optional.of(new User(username, "someavatar")));

        //WHEN
        Optional<User> userByUsername = userService.getUserByUsername(username);

        //THEN
        assertThat(userByUsername.get(), is(new User(username, "someavatar")));
    }

    @Test
    @DisplayName("getUserByUsername should return empty user from Db")
    public void getNotExistingUser() {
        //GIVEN
        String username = "notExistingUserName";
        when(userDb.findById(username)).thenReturn(Optional.empty());

        //WHEN
        Optional<User> userByUsername = userService.getUserByUsername(username);

        //THEN
        assertThat(userByUsername.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Get repositories should return list of github repositories")
    public void getRepositoriesReturnRepositories() {
        //Given
        String username = "mr-foobar";
        when(gitHubApiService.getUserRepos(username)).thenReturn(List.of(
                new GitHubRepo("repository1", "some-url-1"),
                new GitHubRepo("repository2", "some-url-2")
        ));
        when(userDb.existsById(username)).thenReturn(true);

        // When
        Optional<List<UserRepository>> repositories = userService.getRepositories(username);

        // Then
        assertThat(repositories.get(), is(List.of(
                new UserRepository("repository1", "some-url-1"),
                new UserRepository("repository2", "some-url-2")
        )));
        verify(userDb).existsById(username);

    }

    @Test
    @DisplayName("Get repositories should return empty list for non existing user")
    public void getRepositoriesReturnsEmptyList() {
        //Given
        String username = "mr-foobar";
        when(gitHubApiService.getUserRepos(username)).thenReturn(new ArrayList<>());
        when(userDb.existsById(username)).thenReturn(false);

        // When
        Optional<List<UserRepository>> repositories = userService.getRepositories(username);

        // Then
        assertTrue(repositories.isEmpty());
        verify(userDb).existsById(username);

    }
}
