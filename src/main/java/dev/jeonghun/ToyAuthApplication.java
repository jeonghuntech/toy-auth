package dev.jeonghun;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToyAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyAuthApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// TODO Spring Security 적용 후 계정 id 값으로 변경 필요
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
