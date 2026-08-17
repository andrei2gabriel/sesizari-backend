package ro.botosani.ticketing_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    ResponseEntity<?> getUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        JwtUtil jUtil = new JwtUtil();
        String identificator;
        identificator=jUtil.extractIdentificator(token);
        User user = userService.findByEmailOrTelefon(identificator);
        return ResponseEntity.ok(user);
    }
}
