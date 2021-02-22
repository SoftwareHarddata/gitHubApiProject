package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.AddUserDto;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody AddUserDto dto){
        return this.userService.addUser(dto.getName());
    }

    @GetMapping
    public List<User> listUsers(){
        return userService.listUsers();
    }

}
