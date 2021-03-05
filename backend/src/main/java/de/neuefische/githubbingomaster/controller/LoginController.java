package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.LoginDto;
import de.neuefische.githubbingomaster.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("auth")
public class LoginController {


    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("login")
    public String login(@RequestBody LoginDto loginDto){

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }
        return jwtUtils.createToken(loginDto.getUsername(), new HashMap<>(Map.of(
                "course","rem-java-21-1"
        )));
    }
}
