package de.neuefische.githubbingomaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUser {
    private String name;
    private int id;
    @JsonProperty("avatar_url")
    private String avatarURL;
    @JsonProperty("html_url")
    private String htmlURL;
    private int followers;
    private int following;
    private String location;
    private String bio;
}
