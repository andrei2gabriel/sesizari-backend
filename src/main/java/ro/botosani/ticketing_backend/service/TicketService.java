package ro.botosani.ticketing_backend.service;

import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.model.Ticket;
import ro.botosani.ticketing_backend.repository.TicketRepository;
import java.util.List;

@Service // Această adnotare îi spune lui Spring să instanțieze această clasă
public class TicketService {

    private final TicketRepository ticketRepository;

    // Injectăm (aducem) repository-ul prin constructor pentru a-l putea folosi
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Metodă care întoarce toate tichetele
    public List<Ticket> iaToateTichetele() {
        return ticketRepository.findAll();
    }

    // Metodă care salvează un tichet
    public Ticket salveazaTichetNou(Ticket ticket) {
        // Dacă am avea validări complexe (ex: câmpuri goale), le-am pune aici.
        return ticketRepository.save(ticket);
    }
    public void stergeTichet(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticketRepository.delete(ticket);
    }
}