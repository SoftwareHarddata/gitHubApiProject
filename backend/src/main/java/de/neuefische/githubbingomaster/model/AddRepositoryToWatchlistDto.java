package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRepositoryToWatchlistDto {
    private String username;
    private String repositoryName;
}
