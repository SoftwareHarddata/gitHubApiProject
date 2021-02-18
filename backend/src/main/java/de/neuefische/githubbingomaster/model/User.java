package de.neuefische.githubbingomaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String name;
    int id;
    String avatarUrl;
}
