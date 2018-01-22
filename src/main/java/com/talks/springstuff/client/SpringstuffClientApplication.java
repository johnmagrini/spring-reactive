package com.talks.springstuff.client;

import com.talks.springstuff.server.Instrument;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import sun.tools.jar.CommandLine;

@Log
@SpringBootApplication
public class SpringstuffClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringstuffClientApplication.class, args);
	}

	@Bean
	WebClient webclient() {
	    return WebClient.create("http://localhost:8080/instruments");
    }

    CommandLineRunner demo(WebClient webclient) {
	    return strings -> webclient
                .get()
                .uri("")
                .retrieve()
                .bodyToFlux(Instrument.class)
                .filter(inst -> inst.getSymbol().startsWith("X"))
                //.flatMap( inst -> client.get().uri("/{id}/price", inst.getSecurityId())).retrieve.bodytoFlux(Price.class)
                .subscribe(inst -> log.info(inst.toString()));
    }
}

