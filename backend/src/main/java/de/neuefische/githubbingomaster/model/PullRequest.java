package de.neuefische.githubbingomaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequest {

    private String id;
    private String pullRequestTitle;
    private String pullRequestNumber;
    private String pullRequestUrl;
    private String repoOwner;
    private String repoName;

}
