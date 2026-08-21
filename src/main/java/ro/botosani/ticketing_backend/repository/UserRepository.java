package ro.botosani.ticketing_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.botosani.ticketing_backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrTelefon(String email,String telefon);
    User findByEmail(String email);
}
