package backend.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

@SpringBootApplication
public class ResourceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ResourceApplication.class);

		// 설정 파일 추가
		app.addInitializers(applicationContext -> {
			ConfigurableEnvironment env = applicationContext.getEnvironment();
			try {
				env.getPropertySources().addFirst(
						new ResourcePropertySource(new ClassPathResource("application-serverA.properties"))
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		app.run(args);
	}

}
