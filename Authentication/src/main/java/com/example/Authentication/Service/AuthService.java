package com.example.Authentication.Service;

import com.example.Authentication.Entity.User;
import com.example.Authentication.Repostitory.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepo authRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public String addUser(User user) {
        if (authRepo.findByName(user.getName()).isPresent()) {
            return "User Already Exists!";
        }

        // generate and set id BEFORE save
        long seqId = sequenceGeneratorService.generateSeq(User.SEQUENCE_NAME);
        user.setId(seqId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authRepo.save(user);

        return "User Added Successfully";
    }
    public String login(User user) {

        Optional<User> dbUser = authRepo.findByName(user.getName());
        if (dbUser.isEmpty()) {
            return "Invalid Credentials";
        }

        if (passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            return "Login Successful";
        } else {
            return "Invalid Credentials";
        }
    }


}
