package t1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1.dto.UserDto;
import t1.mapper.UserMapper;
import t1.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        final var entity = userMapper.mapToEntity(userDto);
        final var saveUser = userRepository.save(entity);
        return userMapper.mapToDto(saveUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto getUser(Long id) {
        return userRepository.findById(id)
                             .map(userMapper::mapToDto)
                             .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        final var entity = userRepository.findById(id)
                                         .orElseThrow(() -> new EntityNotFoundException("User not found"));
        final var updateUser = userMapper.map(entity, userDto);
        userRepository.save(updateUser);
        return userMapper.mapToDto(updateUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::mapToDto)
                             .toList();
    }
}
