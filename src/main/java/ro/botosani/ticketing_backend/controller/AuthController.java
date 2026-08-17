package ro.botosani.ticketing_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.dto.AccesRequest;
//import ro.botosani.ticketing_backend.dto.AccessRequest;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/acces")
    public ResponseEntity<?> acces(@RequestBody AccesRequest cerere) {
        // 1. Validăm sau înregistrăm cetățeanul automat
        userService.proceseazaAcces(cerere);

        // 2. Generăm cheia criptografică
        String tokenGenerat = jwtUtil.generateToken(cerere.getIdentificator());

        // 3. Trimitem către React exact structura JSON de care are nevoie
        return ResponseEntity.ok(Collections.singletonMap("token", tokenGenerat));
    }
}