package cl.proyecto.Factura.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HospitalizacionClient {

    private final WebClient webClient;

    public String obtenerMotivo(Long codHospitalizacion) {
        try {
            Map response = webClient
                    .get()
                    .uri("/Hospitalizacion/" + codHospitalizacion)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? (String) response.get("motivo") : "Sin hospitalizacion";
        } catch (Exception e) {
            return "Sin hospitalizacion";
        }
    }
}