package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public List<Book> getBooksByUser(@PathVariable String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getBooks();
        }
        return List.of();
    }
}