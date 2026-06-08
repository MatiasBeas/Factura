package cl.proyecto.Factura.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HospitalizacionClient {

    private final WebClient webClientHospitalizacion;

    public String obtenerDiagnostico(Long codHospitalizacion) {
        try {
            Map response = webClientHospitalizacion
                    .get()
                    .uri("/Hospitalizacion/" + codHospitalizacion)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null ? (String) response.get("motivo") : "Sin diagnóstico";

        } catch (Exception e) {
            return "Sin diagnóstico";
        }
    }
}