package cleanbreath.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CleanBreathBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanBreathBackendApplication.class, args);
	}

}
