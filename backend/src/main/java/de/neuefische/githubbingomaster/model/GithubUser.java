package de.neuefische.githubbingomaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {

    @JsonProperty("login")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
