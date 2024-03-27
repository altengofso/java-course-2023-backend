package edu.java.bot.app.repository;

import edu.java.bot.app.models.user.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, User> database = new HashMap<>();

    public User findById(long id) {
        return database.getOrDefault(id, null);
    }

    public User addUser(User user) {
        database.put(user.getId(), user);
        return findById(user.getId());
    }
}
