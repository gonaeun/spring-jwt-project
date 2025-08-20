package com.gonaeun.springjwtproject.repository;

import com.gonaeun.springjwtproject.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Long, User> byId = new HashMap<>();
    private final Map<String, User> byUsername = new HashMap<>();
    private long seq = 0L;

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(byId.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(byUsername.get(username));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++seq);
        }
        byId.put(user.getId(), user);
        byUsername.put(user.getUsername(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(byId.values());
    }
}
