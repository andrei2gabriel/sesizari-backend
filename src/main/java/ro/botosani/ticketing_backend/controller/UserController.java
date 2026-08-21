package ro.botosani.ticketing_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(Principal principal) {
        // Dacă request-ul a trecut de JwtRequestFilter, principal va conține identificatorul
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // principal.getName() returnează email-ul sau telefonul extras din token
        User user = userService.findByEmailOrTelefon(principal.getName());
        return ResponseEntity.ok(user);
    }
}