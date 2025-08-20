package com.gonaeun.springjwtproject.repository;

import com.gonaeun.springjwtproject.domain.User;
import java.util.*;

@org.springframework.stereotype.Repository
public class UserRepository {
    private final Map<String, User> byUsername = new HashMap<>();
    private long seq = 0L;

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(byUsername.get(username));
    }

    public User save(User user) {
        if (user.getId() == null) user.setId(++seq);
        byUsername.put(user.getUsername(), user);
        return user;
    }
}
