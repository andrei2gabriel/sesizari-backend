package ro.botosani.ticketing_backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.dto.AccesRequest;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.CaptchaService;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CaptchaService captchaService;
    private final PasswordEncoder passwordEncoder; // 1. Am adăugat instanța de PasswordEncoder

    // 2. Am injectat PasswordEncoder în constructor
    public AuthController(UserService userService, JwtUtil jwtUtil, CaptchaService captchaService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.captchaService = captchaService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        // Dacă request-ul a trecut de JwtRequestFilter, cookie-ul este valid
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Suprascriem cookie-ul existent cu unul gol și maxAge(0) pentru a-l șterge instant
        ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("mesaj", "Deconectare reușită"));
    }
    @PostMapping("/acces")
    public ResponseEntity<?> acces(@RequestBody AccesRequest cerere) {

        // 1. Validarea CAPTCHA cu API-ul Google
        boolean isCaptchaValid = captchaService.validateCaptcha(cerere.getCaptchaToken());

        if (!isCaptchaValid) {
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

    @PostMapping("/admin-login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String parola = credentials.get("parola");

        Optional<User> userOpt = Optional.ofNullable(userService.findByEmail(email));

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Email incorect.");
        }

        User user = userOpt.get();

        // 1. Verificare rol
        if (!"FUNCTIONAR".equals(user.getRol())) {
            return ResponseEntity.status(403).body("Acces interzis. Nu sunteți funcționar.");
        }

        // 2. Verificare parolă
        if (!passwordEncoder.matches(parola, user.getPassword())) {
            return ResponseEntity.status(401).body("Parolă incorectă.");
        }

        // 3. Generare JWT (am corectat folosind instanța corectă: jwtUtil în loc de jwtService)
        String token = jwtUtil.generateToken(user.getEmail());

        // 4. Configurare HttpOnly Cookie (am adăugat importurile necesare sus)
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true) // <- OBLIGATORIU: Browserul va trimite cookie-ul DOAR pe conexiuni HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 1 zi
                .sameSite("Strict") // <- PROTECȚIE CSRF MAXIMĂ: Cookie-ul pleacă doar dacă utilizatorul este efectiv pe site-ul tău
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("mesaj", "Autentificare reușită"));
    }
}