package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.GithubUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUserDb {

    private ArrayList<GithubUser> userDb = new ArrayList<>();

    public GithubUser addUser(GithubUser user){
        if(!findUser(user)) {userDb.add(user);}
        return user;
    }

    private boolean findUser(GithubUser user) {
        return userDb.contains(user);
    }

}
