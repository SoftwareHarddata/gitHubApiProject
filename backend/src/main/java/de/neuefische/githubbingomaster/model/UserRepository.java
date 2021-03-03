package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRepository {

    private String repositoryName;
    private String repositoryWebUrl;
}