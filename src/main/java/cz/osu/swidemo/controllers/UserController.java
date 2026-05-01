package cz.osu.swidemo.controllers;

import cz.osu.swidemo.dto.UserDTO;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        if (loginData.getUsername() == null || loginData.getPassword() == null) {
            return ResponseEntity.badRequest().body("Chybí jméno nebo heslo");
        }

        return userService.authenticate(loginData.getUsername(), loginData.getPassword())
                .map(user -> ResponseEntity.ok(new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getAge(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRole()
                )))
                .orElse(ResponseEntity.status(401).build());
    }
}