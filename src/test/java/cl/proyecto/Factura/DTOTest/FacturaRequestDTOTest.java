package cl.proyecto.Factura.DTOTest;

import cl.proyecto.Factura.dto.FacturaRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - FacturaRequestDTO")
class FacturaRequestDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FacturaRequestDTO buildDto() {
        return new FacturaRequestDTO(
                BigDecimal.valueOf(100000),
                "PAGADO",
                1L,
                "22.359.190-6"
        );
    }

    @Test
    @DisplayName("GIVEN: AllArgsConstructor WHEN: se construye THEN: todos los campos quedan asignados")
    void allArgsConstructor_shouldAssignAllFields() {

        FacturaRequestDTO dto = buildDto();

        assertThat(dto.getTotalNeto()).isEqualByComparingTo("100000");
        assertThat(dto.getEstadoPago()).isEqualTo("PAGADO");
        assertThat(dto.getCodHospitalizacion()).isEqualTo(1L);
        assertThat(dto.getPacienteRun()).isEqualTo("22.359.190-6");
    }

    @Test
    @DisplayName("GIVEN: NoArgsConstructor WHEN: se construye THEN: campos quedan nulos")
    void noArgsConstructor_shouldLeaveFieldsNull() {

        FacturaRequestDTO dto = new FacturaRequestDTO();

        assertThat(dto.getTotalNeto()).isNull();
        assertThat(dto.getEstadoPago()).isNull();
        assertThat(dto.getCodHospitalizacion()).isNull();
        assertThat(dto.getPacienteRun()).isNull();
    }

    @Test
    @DisplayName("GIVEN: setters WHEN: se invocan THEN: actualizan el estado")
    void setters_shouldUpdateState() {

        FacturaRequestDTO dto = new FacturaRequestDTO();

        dto.setTotalNeto(BigDecimal.valueOf(50000));
        dto.setEstadoPago("PENDIENTE");
        dto.setCodHospitalizacion(2L);
        dto.setPacienteRun("11.111.111-1");

        assertThat(dto.getTotalNeto()).isEqualByComparingTo("50000");
        assertThat(dto.getEstadoPago()).isEqualTo("PENDIENTE");
        assertThat(dto.getCodHospitalizacion()).isEqualTo(2L);
        assertThat(dto.getPacienteRun()).isEqualTo("11.111.111-1");
    }

    @Test
    @DisplayName("GIVEN: dos DTOs iguales WHEN: equals/hashCode THEN: son iguales")
    void equalsAndHashCode_shouldMatch() {

        FacturaRequestDTO dto1 = buildDto();
        FacturaRequestDTO dto2 = buildDto();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("GIVEN: dos DTOs distintos WHEN: equals THEN: no son iguales")
    void equals_shouldDiffer() {

        FacturaRequestDTO dto1 = buildDto();

        FacturaRequestDTO dto2 = new FacturaRequestDTO(
                BigDecimal.valueOf(100000),
                "PENDIENTE",
                1L,
                "22.359.190-6"
        );

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("GIVEN: DTO WHEN: toString THEN: contiene el run")
    void toString_shouldContainRun() {

        FacturaRequestDTO dto = buildDto();

        assertThat(dto.toString()).contains("22.359.190-6");
    }

    @Test
    @DisplayName("GIVEN: DTO WHEN: se serializa THEN: contiene todos los campos")
    void serialize_shouldContainFields() throws Exception {

        String json = objectMapper.writeValueAsString(buildDto());

        ObjectNode node = (ObjectNode) objectMapper.readTree(json);

        assertThat(node.has("totalNeto")).isTrue();
        assertThat(node.has("estadoPago")).isTrue();
        assertThat(node.has("codHospitalizacion")).isTrue();
        assertThat(node.has("pacienteRun")).isTrue();
    }

    @Test
    @DisplayName("GIVEN: JSON valido WHEN: se deserializa THEN: mapea correctamente")
    void deserialize_shouldMapJsonToFields() throws Exception {

        String json = """
            {
              "totalNeto":100000,
              "estadoPago":"PAGADO",
              "codHospitalizacion":1,
              "pacienteRun":"22.359.190-6"
            }
            """;

        FacturaRequestDTO dto =
                objectMapper.readValue(json, FacturaRequestDTO.class);

        assertThat(dto.getTotalNeto()).isEqualByComparingTo("100000");
        assertThat(dto.getEstadoPago()).isEqualTo("PAGADO");
        assertThat(dto.getCodHospitalizacion()).isEqualTo(1L);
        assertThat(dto.getPacienteRun()).isEqualTo("22.359.190-6");
    }

    @Test
    @DisplayName("GIVEN: round-trip WHEN: serializa y deserializa THEN: conserva los datos")
    void roundTrip_shouldPreserveData() throws Exception {

        FacturaRequestDTO original = buildDto();

        String json = objectMapper.writeValueAsString(original);

        FacturaRequestDTO resultado =
                objectMapper.readValue(json, FacturaRequestDTO.class);

        assertThat(resultado).isEqualTo(original);
    }
}