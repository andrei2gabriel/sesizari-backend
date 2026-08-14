package ro.botosani.ticketing_backend.controller;

import org.springframework.web.bind.annotation.*;
import ro.botosani.ticketing_backend.model.Ticket;
import ro.botosani.ticketing_backend.service.TicketService;
import java.util.List;

@RestController // Marchează clasa ca fiind capabilă să primească și să returneze date via HTTP (JSON)
@RequestMapping("/api/tichete") // Toate rutele din această clasă vor începe cu acest prefix
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;


    // Injectăm serviciul pe care l-am creat adineauri
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Această rută se va apela la un GET request pe URL-ul http://localhost:8080/api/tichete
    @GetMapping
    public List<Ticket> afiseazaToate() {
        return ticketService.iaToateTichetele();
    }

    // Această rută se va apela la un POST request pe URL-ul http://localhost:8080/api/tichete
    // @RequestBody transformă JSON-ul trimis de pe web direct într-un obiect de tip Ticket
    @PostMapping
    public Ticket creazaTichet(@RequestBody Ticket ticket) {
        return ticketService.salveazaTichetNou(ticket);
    }

    //Metoda pentru stergerea unui tichet
    @DeleteMapping("/{id}")
    public void stergeTichet(@PathVariable Long id) {
        ticketService.stergeTichet(id);
    }
}