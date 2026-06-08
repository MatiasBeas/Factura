package cl.proyecto.Factura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClientPaciente() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Bean
    public WebClient webClientHospitalizacion() {
        return WebClient.builder()
                .baseUrl("http://localhost:8282")
                .build();
    }


}