package t1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import t1.entity.User;
import t1.service.UserService;

@ComponentScan
public class Main {
    public static void main(String[] args) throws Exception {
        final var context = new AnnotationConfigApplicationContext(Main.class);
        UserService userService = context.getBean(UserService.class);

        userService.createUser("Anton");
        userService.createUser("Sergey");
        userService.createUser("Alex");
        userService.createUser("Petr");

        userService.getAllUsers().forEach(System.out::println);

        assert userService.getAllUsers().size() == 4;

        User sergey = userService.getUser(2L);
        assert sergey.username().equals("Sergey");


        User mari = new User(4L, "Mari");
        userService.updateUser(mari);

        assert userService.getUser(4L).username().equals("Mari");

        userService.deleteUser(4L);
        assert userService.getAllUsers().size() == 3;
    }
}
