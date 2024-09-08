package backend.keumbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeumbangApplication {

	public static void main(String[] args) {
		// SpringApplication.run(KeumbangApplication.class, args);
		String profile = System.getProperty("spring.profiles.active", "serverB");
		SpringApplication app = new SpringApplication(KeumbangApplication.class);
		app.setAdditionalProfiles(profile);
		app.run(args);
	}

}
