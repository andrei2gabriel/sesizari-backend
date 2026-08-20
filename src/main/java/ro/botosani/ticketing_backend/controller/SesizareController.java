package ro.botosani.ticketing_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.service.SesizareService;
import ro.botosani.ticketing_backend.service.UserService;
import ro.botosani.ticketing_backend.util.JwtUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController // Marchează clasa ca fiind capabilă să primească și să returneze date via HTTP (JSON)
@RequestMapping("/api/sesizari") // Toate rutele din această clasă vor începe cu acest prefix
public class SesizareController {

    private final SesizareService SesizareService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    // Injectăm serviciul pe care l-am creat adineauri
    public SesizareController(SesizareService SesizareService, UserService userService, JwtUtil jwtUtil) {
        this.SesizareService = SesizareService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Această rută se va apela la un GET request pe URL-ul http://localhost:8080/api/tichete
    @GetMapping
    public List<Sesizare> afiseazaToate() {
        return SesizareService.iaToateSesizarile();
    }

    // Această rută se va apela la un POST request pe URL-ul http://localhost:8080/api/tichete
    // @RequestBody transformă JSON-ul trimis de pe web direct într-un obiect de tip Sesizare
    @PostMapping
    public Sesizare creazaSesizare(
            @ModelAttribute Sesizare sesizare,
            @RequestParam(value = "imagini", required = false) List<MultipartFile> imagini,
            @RequestHeader("Authorization") String authHeader) {

        // 1. Identificăm utilizatorul din token
        String token = authHeader.substring(7);
        String identificator = jwtUtil.extractIdentificator(token);
        User u = userService.findByEmailOrTelefon(identificator);

        // 2. Ataşăm utilizatorul la entitatea deja asamblată de Spring Boot
        sesizare.setUtilizator(u);

        sesizare.setStare("Nou");
        sesizare.setDataCreare(java.time.LocalDateTime.now());

        // 3. Procesăm și salvăm fișierele binare pe disc
        if (imagini != null && !imagini.isEmpty()) {
            for (MultipartFile imagine : imagini) {
                if (!imagine.isEmpty()) {
                    try {
                        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        // Generăm un UUID pentru a evita conflictele de nume
                        String numeFisier = UUID.randomUUID().toString() + "_" + imagine.getOriginalFilename();
                        Path caleFisier = uploadPath.resolve(numeFisier);
                        Files.write(caleFisier, imagine.getBytes());

                        // Adăugăm calea relativă în lista entității
                        sesizare.getCaiImagini().add("/uploads/" + numeFisier);
                    } catch (Exception e) {
                        throw new RuntimeException("Eroare la scrierea imaginii pe disc", e);
                    }
                }
            }
        }

        // 4. Salvăm totul în baza de date
        return SesizareService.salveazaSesizareNoua(sesizare);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getSesizaribyUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String identificator;
        identificator=jwtUtil.extractIdentificator(token);
        User u=userService.findByEmailOrTelefon(identificator);
        Long id=u.getId();
        List<Sesizare> result= SesizareService.getSesizariByUser(id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    public Sesizare schimbaStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> dateActualizare) {

        String stareNoua = dateActualizare.get("stare");
        String mesaj = dateActualizare.get("mesajDispecer");

        return SesizareService.actualizeazaStareSiMesaj(id, stareNoua, mesaj);
    }
    //Metoda pentru stergerea unui tichet
    @DeleteMapping("/{id}")
    public void stergeSesizare(@PathVariable Long id) {
        SesizareService.stergeSesizare(id);
    }
}