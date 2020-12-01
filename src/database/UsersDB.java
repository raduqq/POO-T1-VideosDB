package database;

import user.User;

import java.util.ArrayList;
import java.util.List;

public final class UsersDB {
    private final List<User> userList;

    public UsersDB() {
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    /**
     *
     * @param user to be added into the database
     */
    public void addToDB(final User user) {
        userList.add(user);
    }

    /**
     *
     * @param username to be matched with a user in the database
     * @return user with the given username
     */
    public User findUserByUsername(final String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }
}
