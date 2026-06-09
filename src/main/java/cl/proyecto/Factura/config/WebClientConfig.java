package cl.proyecto.Factura.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${webclient.paciente.url}")
    private String pacienteUrl;

    @Value("${webclient.hospitalizacion.url}")
    private String hospitalizacionUrl;

    @Bean
    public WebClient webClientPaciente() {
        return WebClient.builder()
                .baseUrl(pacienteUrl)
                .build();
    }

    @Bean
    public WebClient webClientHospitalizacion() {
        return WebClient.builder()
                .baseUrl(hospitalizacionUrl)
                .build();
    }
}