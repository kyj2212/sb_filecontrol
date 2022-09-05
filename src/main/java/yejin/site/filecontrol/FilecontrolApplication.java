package yejin.site.filecontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 이거는 왜 하는걸까?
public class FilecontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilecontrolApplication.class, args);
	}

}
