package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.GithubUser;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class GithubUserDbTest {

    @Test
    public void addUserShouldAddAndReturnUser(){
        // Given
        GithubUser magnus = new GithubUser("magnus", "http://awesomepics.com/magnus");
        GithubUserDb testDb = new GithubUserDb();

        // When
        testDb.addUser(magnus);

        // Then
        assertThat(testDb.getUserDb(), containsInAnyOrder(new GithubUser("magnus", "http://awesomepics.com/magnus")));


    }

    @Test
    public void addUserShouldNotAddDuplicateAndReturnUser(){
        // Given
        GithubUser magnus = new GithubUser("magnus", "http://awesomepics.com/magnus");
        GithubUserDb testDb = new GithubUserDb();

        // When
        testDb.addUser(magnus);
        testDb.addUser(magnus);

        // Then
        assertThat(testDb.getUserDb(), containsInAnyOrder(new GithubUser("magnus", "http://awesomepics.com/magnus")));
        assertThat(testDb.getUserDb().size(), is(1));
    }
}