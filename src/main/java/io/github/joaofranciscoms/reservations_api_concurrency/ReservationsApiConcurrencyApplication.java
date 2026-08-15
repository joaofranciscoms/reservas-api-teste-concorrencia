package io.github.joaofranciscoms.reservations_api_concurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReservationsApiConcurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationsApiConcurrencyApplication.class, args);
	}
}
