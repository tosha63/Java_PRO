package t1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1.entity.User;
import t1.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void updateUser(Long id, String newUsername, int age) {
        userRepository.updateUser(id, newUsername, age);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
