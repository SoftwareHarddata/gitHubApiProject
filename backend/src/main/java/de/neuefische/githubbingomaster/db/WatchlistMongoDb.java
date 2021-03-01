package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WatchlistMongoDb extends PagingAndSortingRepository<WatchlistRepository,String> {
    List<WatchlistRepository> findAll();
}
