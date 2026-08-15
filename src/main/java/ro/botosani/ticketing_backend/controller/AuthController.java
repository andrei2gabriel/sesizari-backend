package ro.botosani.ticketing_backend.controller;


import io.jsonwebtoken.JwsHeader;
import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.dto.LoginRequest;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;
    JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequest user) {
        String identificator=user.getIdentificator();
        String password=user.getPassword();
        User logare= userService.loginUser(identificator, password);
        if(logare!=null)
        {
            String tokenGenerat=jwtUtil.generateToken(identificator);
            return Collections.singletonMap("token", tokenGenerat);
        }
        else {
            return Collections.emptyMap();
        }
    }
}
