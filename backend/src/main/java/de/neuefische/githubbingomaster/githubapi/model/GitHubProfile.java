package de.neuefische.githubbingomaster.githubapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubProfile {

    private String login;
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
