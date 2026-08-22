package ro.botosani.ticketing_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.SesizareService;
import ro.botosani.ticketing_backend.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sesizari")
public class SesizareController {

    private final SesizareService sesizareService;
    private final UserService userService;

    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    public SesizareController(SesizareService sesizareService, UserService userService) {
        this.sesizareService = sesizareService;
        this.userService = userService;
    }

    @GetMapping
    public List<Sesizare> afiseazaToate() {
        return sesizareService.iaToateSesizarile();
    }

    @PostMapping
    public Sesizare creazaSesizare(
            @ModelAttribute Sesizare sesizare,
            @RequestParam(value = "imagini", required = false) List<MultipartFile> imagini,
            Principal principal) {

        if (principal == null) {
            throw new RuntimeException("Utilizator neautentificat");
        }
        User u = userService.findByEmailOrTelefon(principal.getName());

        sesizare.setUtilizator(u);
        sesizare.setStare("Nou");
        sesizare.setDataCreare(java.time.LocalDateTime.now());

        // Definim tipurile MIME acceptate
        List<String> tipuriPermise = List.of("image/jpeg", "image/png", "image/webp");

        if (imagini != null && !imagini.isEmpty()) {
            for (MultipartFile imagine : imagini) {
                if (!imagine.isEmpty()) {

                    // 1. Validare Content-Type
                    String contentType = imagine.getContentType();
                    if (contentType == null || !tipuriPermise.contains(contentType)) {
                        throw new RuntimeException("Tip de fișier respins. Se acceptă exclusiv JPEG, PNG sau WEBP.");
                    }

                    try {
                        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        String numeFisier = UUID.randomUUID().toString() + "_" + imagine.getOriginalFilename();
                        Path caleFisier = uploadPath.resolve(numeFisier);
                        Files.write(caleFisier, imagine.getBytes());
                        sesizare.getCaiImagini().add("/uploads/" + numeFisier);
                    } catch (Exception e) {
                        throw new RuntimeException("Eroare la scrierea imaginii pe disc", e);
                    }
                }
            }
        }

        return sesizareService.salveazaSesizareNoua(sesizare);
    }
    @GetMapping("/user")
    public ResponseEntity<?> getSesizaribyUser(Principal principal) { // <-- Folosim Principal și aici
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User u = userService.findByEmailOrTelefon(principal.getName());
        Long id = u.getId();
        List<Sesizare> result = sesizareService.getSesizariByUser(id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    public Sesizare schimbaStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> dateActualizare) {
        String stareNoua = dateActualizare.get("stare");
        String mesaj = dateActualizare.get("mesajDispecer");
        return sesizareService.actualizeazaStareSiMesaj(id, stareNoua, mesaj);
    }

    @DeleteMapping("/{id}")
    public void stergeSesizare(@PathVariable Long id) {
        sesizareService.stergeSesizare(id);
    }
}