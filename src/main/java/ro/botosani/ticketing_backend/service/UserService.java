package ro.botosani.ticketing_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.repository.UserRepository;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        if(user.getEmail()==null || user.getTelefon()==null)
        {
            throw new IllegalArgumentException("Trebuie introdus un email si un telefon.");
        }
        else {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        }
    }

    public User loginUser(String identificator, String password) {
        User rezultat=userRepository.findByEmailOrTelefon(identificator, identificator);
        if(rezultat==null)
        {
            throw new RuntimeException("Cont inexistent.");
        }
        else {
            String passwordCripted = rezultat.getPassword();
            if (passwordEncoder.matches(password, passwordCripted)) {
                return rezultat;
            }
            else {
                throw new RuntimeException("Parola incorecta.");
            }
        }
    }
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }
}
