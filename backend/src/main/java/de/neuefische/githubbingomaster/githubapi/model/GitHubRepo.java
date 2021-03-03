package de.neuefische.githubbingomaster.githubapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepo {

    @JsonProperty("name")
    private String repository;
    @JsonProperty("html_url")
    private String repositoryUrl;
}