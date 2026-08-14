package ro.botosani.ticketing_backend.controller;

import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.model.Sesizare;
import ro.botosani.ticketing_backend.service.SesizareService;
import java.util.List;

@RestController // Marchează clasa ca fiind capabilă să primească și să returneze date via HTTP (JSON)
@RequestMapping("/api/sesizari") // Toate rutele din această clasă vor începe cu acest prefix
@CrossOrigin(origins = "*")
public class SesizareController {

    private final SesizareService SesizareService;


    // Injectăm serviciul pe care l-am creat adineauri
    public SesizareController(SesizareService SesizareService) {
        this.SesizareService = SesizareService;
    }

    // Această rută se va apela la un GET request pe URL-ul http://localhost:8080/api/tichete
    @GetMapping
    public List<Sesizare> afiseazaToate() {
        return SesizareService.iaToateSesizarile();
    }

    // Această rută se va apela la un POST request pe URL-ul http://localhost:8080/api/tichete
    // @RequestBody transformă JSON-ul trimis de pe web direct într-un obiect de tip Sesizare
    @PostMapping
    public Sesizare creazaSesizare(@RequestBody Sesizare sesizare) {
        return SesizareService.salveazaSesizareNoua(sesizare);
    }

    //Metoda pentru stergerea unui tichet
    @DeleteMapping("/{id}")
    public void stergeSesizare(@PathVariable Long id) {
        SesizareService.stergeSesizare(id);
    }
}