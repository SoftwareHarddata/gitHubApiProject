package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubUser {

    private String name;
    private String avatarUrl;

}
