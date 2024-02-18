package edu.java.bot.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, User> database = new HashMap<>();

    public User findById(Long id) {
        return database.get(id);
    }

    public void addUser(User user) {
        database.put(user.getId(), user);
    }
}
