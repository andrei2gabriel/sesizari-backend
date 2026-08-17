package ro.botosani.ticketing_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.botosani.ticketing_backend.model.Sesizare;

import java.util.List;

public interface SesizareRepository extends JpaRepository<Sesizare, Long> {
    List<Sesizare> findByUtilizatorId(Long id);
}
