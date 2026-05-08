package org.example.gym_management;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymManagmentApplication {

    public static void main(String[] args) {
        // Carica il file app.env | Load the app.env file
        Dotenv dotenv = Dotenv.configure()
                .filename("app.env") // Specifica il nome se non è il classico .env | Specify the name if it's not classic .env
                .load();

        // Le espone come proprietà di sistema per Spring | Makes it a system property
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(GymManagmentApplication.class, args);
    }

}
