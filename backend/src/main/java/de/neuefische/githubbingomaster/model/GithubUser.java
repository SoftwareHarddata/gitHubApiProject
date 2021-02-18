package de.neuefische.githubbingomaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubUser {

    @JsonProperty("login")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
