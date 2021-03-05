package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.WatchlistMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.UserRepository;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class WatchListServiceTest {

    private final GitHubApiService gitHubApiService = mock(GitHubApiService.class);
    private final WatchlistMongoDb watchlistMongoDb = mock(WatchlistMongoDb.class);
    private final WatchListService watchListService = new WatchListService(gitHubApiService, watchlistMongoDb);

    @Test
    @DisplayName("Add Repository to Watchlist")
    public void addRepositoryToWatchlist() {
        //Given

        String userName = "GrafZahl";
        String repoName = "repoName";

        GitHubRepo testRepo = GitHubRepo.builder()
                .id("123")
                .repository("repo-name")
                .repositoryUrl("repo-url")
                .build();

        when(gitHubApiService.getRepository(userName, repoName)).thenReturn(Optional.of(testRepo));
        when(watchlistMongoDb.existsById("123")).thenReturn(false);


        //When
        Optional<UserRepository> actual = watchListService.addToWatchlist(userName, repoName);
        //Then
        assertThat(actual.get(), is(UserRepository.builder()
                .id("123")
                .repositoryName("repo-name")
                .repositoryWebUrl("repo-url")
                .onWatchlist(true)
                .build()));
        verify(watchlistMongoDb).save(WatchlistRepository.builder()
                .id("123")
                .repositoryName("repo-name")
                .repositoryWebUrl("repo-url")
                .build());
    }


    @Test
    @DisplayName("Add not existing Repository to Watchlist returns empty optional")
    public void addNotExistingRepositoryToWatchlist() {
        //Given

        String userName = "GrafZahl";
        String repoName = "repoName";

        when(gitHubApiService.getRepository(userName, repoName)).thenReturn(Optional.empty());

        //When
        Optional<UserRepository> actual = watchListService.addToWatchlist(userName, repoName);
        //Then
        assertThat(actual.isEmpty(), is(true));
    }


    @Test
    @DisplayName("Add Repository to Watchlist that is already on watch list")
    public void addRepositoryToWatchlistThatIsOnWatchlist() {
        //Given

        String userName = "GrafZahl";
        String repoName = "repoName";

        GitHubRepo testRepo = GitHubRepo.builder()
                .id("123")
                .repository("repo-name")
                .repositoryUrl("repo-url")
                .build();

        when(gitHubApiService.getRepository(userName, repoName)).thenReturn(Optional.of(testRepo));
        when(watchlistMongoDb.existsById("123")).thenReturn(true);


        //When
        Optional<UserRepository> actual = watchListService.addToWatchlist(userName, repoName);
        //Then
        assertThat(actual.get(), is(UserRepository.builder()
                .id("123")
                .repositoryName("repo-name")
                .repositoryWebUrl("repo-url")
                .onWatchlist(true)
                .build()));
        verify(watchlistMongoDb, times(0)).save(WatchlistRepository.builder()
                .id("123")
                .repositoryName("repo-name")
                .repositoryWebUrl("repo-url")
                .build());
    }

    @Test
    @DisplayName("Delete from watchlist deletes item from db")
    public void deleteFromWatchList() {

        //WHEN
        watchListService.deleteFromWatchlist("123");

        //THEN
        verify(watchlistMongoDb).deleteById("123");
    }

    @Test
    @DisplayName("getWatchList should return all items from db")
    public void getWatchList() {
        //GIVEN
        when(watchlistMongoDb.findAll()).thenReturn(
                List.of(WatchlistRepository.builder()
                        .id("123")
                        .repositoryName("repo-name")
                        .repositoryWebUrl("repo-url")
                        .build(), WatchlistRepository.builder()
                        .id("345")
                        .repositoryName("repo-name2")
                        .repositoryWebUrl("repo-url2")
                        .build())
        );
        //WHEN
        List<UserRepository> watchlist = watchListService.getWatchlist();

        //THEN
        assertThat(watchlist, containsInAnyOrder(
                UserRepository.builder()
                        .id("123")
                        .repositoryName("repo-name")
                        .repositoryWebUrl("repo-url")
                        .onWatchlist(true)
                        .build(),
                UserRepository.builder()
                        .id("345")
                        .repositoryName("repo-name2")
                        .repositoryWebUrl("repo-url2")
                        .onWatchlist(true)
                        .build()
        ));
    }

}
