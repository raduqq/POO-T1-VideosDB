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
}
