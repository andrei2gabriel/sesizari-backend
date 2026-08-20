package ro.botosani.ticketing_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.dto.AccesRequest;
import ro.botosani.ticketing_backend.service.CaptchaService;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CaptchaService captchaService;

    // Am injectat CaptchaService alături de celelalte dependințe
    public AuthController(UserService userService, JwtUtil jwtUtil, CaptchaService captchaService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.captchaService = captchaService;
    }

    @PostMapping("/acces")
    public ResponseEntity<?> acces(@RequestBody AccesRequest cerere) {

        // 1. Validarea CAPTCHA cu API-ul Google
        boolean isCaptchaValid = captchaService.validateCaptcha(cerere.getCaptchaToken());

        if (!isCaptchaValid) {
            // Returnăm 400 Bad Request sub formă de JSON pentru a putea fi citit ușor de React
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("eroare", "Validarea CAPTCHA a eșuat. Cerere respinsă."));
        }

        // 2. Validăm sau înregistrăm cetățeanul automat
        userService.proceseazaAcces(cerere);

        // 3. Generăm cheia criptografică
        String tokenGenerat = jwtUtil.generateToken(cerere.getIdentificator());

        // 4. Trimitem către React exact structura JSON de care are nevoie
        return ResponseEntity.ok(Collections.singletonMap("token", tokenGenerat));
    }
}