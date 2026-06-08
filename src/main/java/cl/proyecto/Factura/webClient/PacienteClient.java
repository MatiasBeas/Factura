package cl.proyecto.Factura.webClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PacienteClient {

    private final WebClient webClientPaciente;

    public String obtenerNombreCompleto(String run) {
        try {
            Map response = webClientPaciente
                    .get()
                    .uri("/pacientes/" + run)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Respuesta Paciente: " + response);

            if (response == null) return "Sin nombre";

            String pNombre = (String) response.get("pnombre");
            String pApellido = (String) response.get("papellido");
            return pNombre + " " + pApellido;

        } catch (Exception e) {
            return "Sin nombre";
        }
    }

    public boolean existePaciente(String run) {
        try {
            Map response = webClientPaciente
                    .get()
                    .uri("/pacientes/" + run)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null && response.get("run") != null;
        } catch (Exception e) {
            return false;
        }
    }
}