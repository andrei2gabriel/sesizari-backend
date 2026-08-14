package ro.botosani.ticketing_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.botosani.ticketing_backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
