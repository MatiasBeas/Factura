package cl.proyecto.Factura.DTOTest;

import cl.proyecto.Factura.dto.FacturaResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - FacturaResponseDTO")
class FacturaResponseDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FacturaResponseDTO buildDto() {

        return new FacturaResponseDTO(
                1L,
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(19000),
                BigDecimal.valueOf(119000),
                "PAGADO",
                1L,
                "Brazo roto",
                "22.359.190-6",
                "Matias"
        );
    }

    @Test
    @DisplayName("GIVEN: AllArgsConstructor WHEN: se construye THEN: todos los campos quedan asignados")
    void allArgsConstructor_shouldAssignAllFields() {

        FacturaResponseDTO dto = buildDto();

        assertThat(dto.getNumFactura()).isEqualTo(1L);
        assertThat(dto.getTotalNeto()).isEqualByComparingTo("100000");
        assertThat(dto.getIva()).isEqualByComparingTo("19000");
        assertThat(dto.getTotalPagar()).isEqualByComparingTo("119000");
        assertThat(dto.getEstadoPago()).isEqualTo("PAGADO");
        assertThat(dto.getCodHospitalizacion()).isEqualTo(1L);
        assertThat(dto.getDiagnostico()).isEqualTo("Brazo roto");
        assertThat(dto.getPacienteRun()).isEqualTo("22.359.190-6");
        assertThat(dto.getNombrePaciente()).isEqualTo("Matias");
    }

    @Test
    @DisplayName("GIVEN: NoArgsConstructor WHEN: se construye THEN: campos quedan nulos")
    void noArgsConstructor_shouldLeaveFieldsNull() {

        FacturaResponseDTO dto = new FacturaResponseDTO();

        assertThat(dto.getNumFactura()).isNull();
        assertThat(dto.getTotalNeto()).isNull();
        assertThat(dto.getIva()).isNull();
        assertThat(dto.getTotalPagar()).isNull();
        assertThat(dto.getEstadoPago()).isNull();
        assertThat(dto.getCodHospitalizacion()).isNull();
        assertThat(dto.getDiagnostico()).isNull();
        assertThat(dto.getPacienteRun()).isNull();
        assertThat(dto.getNombrePaciente()).isNull();
    }

    @Test
    @DisplayName("GIVEN: setters WHEN: se invocan THEN: actualizan el estado")
    void setters_shouldUpdateState() {

        FacturaResponseDTO dto = new FacturaResponseDTO();

        dto.setNumFactura(10L);
        dto.setTotalNeto(BigDecimal.valueOf(50000));
        dto.setIva(BigDecimal.valueOf(9500));
        dto.setTotalPagar(BigDecimal.valueOf(59500));
        dto.setEstadoPago("PENDIENTE");
        dto.setCodHospitalizacion(2L);
        dto.setDiagnostico("Gripe");
        dto.setPacienteRun("11.111.111-1");
        dto.setNombrePaciente("Carlos");

        assertThat(dto.getNumFactura()).isEqualTo(10L);
        assertThat(dto.getTotalPagar()).isEqualByComparingTo("59500");
        assertThat(dto.getDiagnostico()).isEqualTo("Gripe");
        assertThat(dto.getNombrePaciente()).isEqualTo("Carlos");
    }

    @Test
    void equalsAndHashCode_shouldMatch() {

        FacturaResponseDTO dto1 = buildDto();
        FacturaResponseDTO dto2 = buildDto();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void equals_shouldDiffer() {

        FacturaResponseDTO dto1 = buildDto();

        FacturaResponseDTO dto2 = buildDto();
        dto2.setEstadoPago("PENDIENTE");

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void toString_shouldContainFacturaNumber() {

        FacturaResponseDTO dto = buildDto();

        assertThat(dto.toString()).contains("1");
    }

    @Test
    void serialize_shouldContainAllFields() throws Exception {

        String json = objectMapper.writeValueAsString(buildDto());

        ObjectNode node = (ObjectNode) objectMapper.readTree(json);

        assertThat(node.has("numFactura")).isTrue();
        assertThat(node.has("totalNeto")).isTrue();
        assertThat(node.has("iva")).isTrue();
        assertThat(node.has("totalPagar")).isTrue();
        assertThat(node.has("estadoPago")).isTrue();
        assertThat(node.has("codHospitalizacion")).isTrue();
        assertThat(node.has("diagnostico")).isTrue();
        assertThat(node.has("pacienteRun")).isTrue();
        assertThat(node.has("nombrePaciente")).isTrue();
    }

    @Test
    void roundTrip_shouldPreserveData() throws Exception {

        FacturaResponseDTO original = buildDto();

        String json = objectMapper.writeValueAsString(original);

        FacturaResponseDTO resultado =
                objectMapper.readValue(json, FacturaResponseDTO.class);

        assertThat(resultado).isEqualTo(original);
    }
}