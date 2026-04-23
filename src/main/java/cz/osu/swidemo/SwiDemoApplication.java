package cz.osu.swidemo;

import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import cz.osu.swidemo.services.UserService;

@SpringBootApplication
public class SwiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiDemoApplication.class, args);
    }

@Bean
CommandLineRunner initDatabase(UserRepository userRepository, UserService userService) {
    return args -> {
        userRepository.deleteAll();

            User u = new User();
            u.setUsername("admin_erika");
            u.setPassword("top_ultramegagigatajne_habibiheslo");
            u.setFirstName("Erika");
            u.setLastName("Feldmannova");
            u.setAge(20);
            u.setEmail("erika@student.osu.cz");

            userService.registerNewUser(u);


            System.out.println("Propojeno. Uživatel 'admin_erika' byl uložen.");

    };
}
}


