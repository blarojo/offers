package uk.worldpay.offers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("uk.worldpay.offers")
public class OffersApplication {

	public static void main(String[] args) {
		SpringApplication.run(OffersApplication.class, args);
	}

}
