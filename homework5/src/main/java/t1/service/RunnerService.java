package t1.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import t1.entity.User;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunnerService implements CommandLineRunner {

    private final UserService userService;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        userService.createUser(new User(faker.name().firstName(), generateRandomAge()));

        log.info("All users: {}", userService.getAllUsers());

        User user = userService.getUser(2L);

        log.info("Before Update user {}", user);
        userService.updateUser(user.getId(), faker.name().firstName(), generateRandomAge());
        log.info("After Update user {}", userService.getUser(2L));

        userService.deleteUser(4L);
    }

    private int generateRandomAge() {
        return random.nextInt(81) + 10;
    }
}
