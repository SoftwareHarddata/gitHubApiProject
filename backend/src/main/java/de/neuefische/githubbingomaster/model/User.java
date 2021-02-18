package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String name;
    String login;
    int id;
    String avatarUrl;
}
