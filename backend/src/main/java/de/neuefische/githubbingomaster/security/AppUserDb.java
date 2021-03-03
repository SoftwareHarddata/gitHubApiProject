package de.neuefische.githubbingomaster.security;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AppUserDb extends PagingAndSortingRepository<AppUser, String> {
}
