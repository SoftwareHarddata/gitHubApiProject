package de.neuefische.githubbingomaster.githubapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitHubPullRequest {

    @Id
    private String id;
    @JsonProperty("title")
    private String pullRequestTitle;
    @JsonProperty("number")
    private String pullRequestNumber;
    @JsonProperty("html_url")
    private String pullRequestUrl;
    private String repoOwner;
    private String repoName;

    @SuppressWarnings("unchecked")
    @JsonProperty("head")
    private void unpackNested(Map<String, Object> head) {
        Map<String, String> headUserMap = (Map<String, String>) head.get("user");
        this.repoOwner = headUserMap.get("login");
        Map<String, String> headRepoMap = (Map<String, String>) head.get("repo");
        this.repoName = headRepoMap.get("name");
    }

}