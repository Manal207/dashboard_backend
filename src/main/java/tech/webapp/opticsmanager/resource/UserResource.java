package tech.webapp.opticsmanager.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.webapp.opticsmanager.model.User;
import tech.webapp.opticsmanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;
    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
