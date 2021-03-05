package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="watchlist")
public class WatchlistRepository {

    @Id
    private String id;
    private String repositoryWebUrl;
    private String repositoryName;
}
