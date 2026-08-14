package ro.botosani.ticketing_backend.service;

import ro.botosani.ticketing_backend.model.User;
import ro.botosani.ticketing_backend.repository.UserRepository;

import java.util.List;

public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }
}
