package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;

class UserDbTest {

    private final UserDb userDb = new UserDb();

    @Test
    @DisplayName("Add should insert users into list")
    public void addUser() {
        //GIVEN
        User user = new User("supergithubuser", "someavatar");

        //WHEN
        User userResponse = userDb.addUser(user);
        List<User> users = userDb.list();

        //THEN
        assertThat(userResponse, is(new User("supergithubuser", "someavatar")));
        assertThat(users, contains(new User("supergithubuser", "someavatar")));
    }

    @Test
    @DisplayName("findByUsername should return user with matching username")
    public void findByUsername(){
        //GIVEN
        userDb.addUser(new User("supergithubuser", "someavatar1"));
        userDb.addUser(new User("otherUsername", "someavatar2"));

        //WHEN
        Optional<User> userResponse = userDb.findByUsername("otherUsername");

        //THEN
        assertThat(userResponse.get(), is(new User("otherUsername", "someavatar2")));

    }


    @Test
    @DisplayName("findByUsername should return empty optional when username not found")
    public void findByUsernameNotFound(){
        //GIVEN
        userDb.addUser(new User("supergithubuser", "someavatar1"));
        userDb.addUser(new User("otherUsername", "someavatar2"));

        //WHEN
        Optional<User> userResponse = userDb.findByUsername("unknownUsername");

        //THEN
        assertThat(userResponse.isEmpty(), is(true));

    }

}
