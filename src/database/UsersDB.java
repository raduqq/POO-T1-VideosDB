package database;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class UsersDB {
    private List<User> userList;

    public UsersDB() { userList = new ArrayList<>(); }

    public List<User> getUserList() {
        return userList;
    }

    public void addToDB(User user) {
        userList.add(user);
    }

    public User findUserByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "UsersDB{" +
                "userList=" + userList +
                '}';
    }
}
