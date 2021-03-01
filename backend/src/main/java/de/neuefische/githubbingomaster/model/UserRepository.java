package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRepository {

    private String id;
    private String repositoryName;
    private String repositoryWebUrl;
    private boolean onWatchlist;
}
