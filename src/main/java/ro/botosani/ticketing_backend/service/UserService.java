package ro.botosani.ticketing_backend.service;

import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.dto.AccesRequest;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User proceseazaAcces(AccesRequest cerere) {
        // Căutăm dacă cetățeanul există deja
        User rezultat = userRepository.findByEmailOrTelefon(cerere.getIdentificator(), cerere.getIdentificator());

        if (rezultat != null) {
            return rezultat;
        }

        // Nu există, îl creăm acum
        User noulUser = new User();
        noulUser.setNume(cerere.getNume());
        noulUser.setPrenume(cerere.getPrenume());
        if (cerere.getIdentificator().contains("@"))
        {
            // Salvăm numărul sau adresa aici
            noulUser.setEmail(cerere.getIdentificator());
        }
        else
        {
            noulUser.setTelefon(cerere.getIdentificator());
        }

        return userRepository.save(noulUser);
    }

    public User findByEmailOrTelefon(String identificator) {
            return userRepository.findByEmailOrTelefon(identificator, identificator);
    }

    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }


}