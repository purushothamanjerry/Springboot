package com.example.Authentication.Repostitory;

import com.example.Authentication.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
public interface AuthRepo extends MongoRepository<User, Long> {
    Optional<User> findByName(String name);

}
