package backend.keumbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

@SpringBootApplication
public class KeumbangApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KeumbangApplication.class);

		// 설정 파일 추가
		app.addInitializers(applicationContext -> {
			ConfigurableEnvironment env = applicationContext.getEnvironment();
			try {
				env.getPropertySources().addFirst(
						new ResourcePropertySource(new ClassPathResource("application-serverB.properties"))
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		app.run(args);
	}

}
