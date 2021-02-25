package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserMongoDb extends PagingAndSortingRepository<User,String> {

    List<User> findAll();
}
