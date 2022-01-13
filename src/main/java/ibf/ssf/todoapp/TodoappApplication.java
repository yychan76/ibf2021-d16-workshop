package ibf.ssf.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoappApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TodoappApplication.class);

		app.run(args);
	}

}
