package cl.proyecto.Factura.ServiceTest;

import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.model.Factura;
import cl.proyecto.Factura.repository.FacturaRepository;
import cl.proyecto.Factura.service.FacturaService;
import cl.proyecto.Factura.webClient.HospitalizacionClient;
import cl.proyecto.Factura.webClient.PacienteClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - FacturaService")
class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private HospitalizacionClient hospitalizacionClient;

    @InjectMocks
    private FacturaService facturaService;

    private Factura crearFactura() {
        return new Factura(
                1L,
                new BigDecimal("100000"),
                new BigDecimal("19000"),
                new BigDecimal("119000"),
                "PAGADO",
                1L,
                "22.359.190-6"
        );
    }

    private FacturaRequestDTO crearRequest() {
        return new FacturaRequestDTO(
                new BigDecimal("100000"),
                "PAGADO",
                1L,
                "22.359.190-6"
        );
    }

    @Test
    @DisplayName("GIVEN: facturas existentes WHEN: obtenerTodos THEN: retorna lista DTO")
    void obtenerTodos_shouldReturnList() {

        when(facturaRepository.findAll())
                .thenReturn(List.of(crearFactura()));

        when(pacienteClient.obtenerNombreCompleto(anyString()))
                .thenReturn("Juan Perez");

        when(hospitalizacionClient.obtenerDiagnostico(anyLong()))
                .thenReturn("Fractura");

        List<FacturaResponseDTO> resultado =
                facturaService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getNombrePaciente())
                .isEqualTo("Juan Perez");
    }

    @Test
    @DisplayName("GIVEN: id existente WHEN: obtenerPorId THEN: retorna DTO")
    void obtenerPorId_shouldReturnFactura() {

        when(facturaRepository.findById(1L))
                .thenReturn(Optional.of(crearFactura()));

        when(pacienteClient.obtenerNombreCompleto(anyString()))
                .thenReturn("Juan Perez");

        when(hospitalizacionClient.obtenerDiagnostico(anyLong()))
                .thenReturn("Fractura");

        Optional<FacturaResponseDTO> resultado =
                facturaService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNumFactura()).isEqualTo(1L);
    }

    @Test
    @DisplayName("GIVEN: estado existente WHEN: obtenerPorEstado THEN: retorna lista")
    void obtenerPorEstado_shouldReturnList() {

        when(facturaRepository.findByEstadoPago("PAGADO"))
                .thenReturn(List.of(crearFactura()));

        when(pacienteClient.obtenerNombreCompleto(anyString()))
                .thenReturn("Juan Perez");

        when(hospitalizacionClient.obtenerDiagnostico(anyLong()))
                .thenReturn("Fractura");

        List<FacturaResponseDTO> resultado =
                facturaService.obtenerPorEstado("PAGADO");

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("GIVEN: paciente inexistente WHEN: guardar THEN: lanza excepcion")
    void guardar_shouldThrowWhenPacienteNotExists() {

        FacturaRequestDTO dto = crearRequest();

        when(pacienteClient.existePaciente(anyString()))
                .thenReturn(false);

        assertThatThrownBy(() ->
                facturaService.guardar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe un paciente");
    }

    @Test
    @DisplayName("GIVEN: historial inexistente WHEN: guardar THEN: lanza excepcion")
    void guardar_shouldThrowWhenHistorialNotExists() {

        FacturaRequestDTO dto = crearRequest();

        when(pacienteClient.existePaciente(anyString()))
                .thenReturn(true);

        when(hospitalizacionClient.existeHistorial(anyLong()))
                .thenReturn(false);

        assertThatThrownBy(() ->
                facturaService.guardar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe un Historial");
    }

    @Test
    @DisplayName("GIVEN: datos validos WHEN: guardar THEN: calcula IVA y guarda")
    void guardar_shouldCalculateAndSave() {

        FacturaRequestDTO dto = crearRequest();

        when(pacienteClient.existePaciente(anyString()))
                .thenReturn(true);

        when(hospitalizacionClient.existeHistorial(anyLong()))
                .thenReturn(true);

        when(facturaRepository.save(any(Factura.class)))
                .thenReturn(crearFactura());

        when(pacienteClient.obtenerNombreCompleto(anyString()))
                .thenReturn("Juan Perez");

        when(hospitalizacionClient.obtenerDiagnostico(anyLong()))
                .thenReturn("Fractura");

        FacturaResponseDTO resultado =
                facturaService.guardar(dto);

        assertThat(resultado.getIva())
                .isEqualByComparingTo("19000");

        assertThat(resultado.getTotalPagar())
                .isEqualByComparingTo("119000");

        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    @DisplayName("GIVEN: factura existente WHEN: actualizar THEN: retorna DTO actualizado")
    void actualizar_shouldUpdateFactura() {

        FacturaRequestDTO dto = crearRequest();

        when(pacienteClient.existePaciente(anyString()))
                .thenReturn(true);

        when(hospitalizacionClient.existeHistorial(anyLong()))
                .thenReturn(true);

        when(facturaRepository.findById(1L))
                .thenReturn(Optional.of(crearFactura()));

        when(facturaRepository.save(any(Factura.class)))
                .thenReturn(crearFactura());

        when(pacienteClient.obtenerNombreCompleto(anyString()))
                .thenReturn("Juan Perez");

        when(hospitalizacionClient.obtenerDiagnostico(anyLong()))
                .thenReturn("Fractura");

        Optional<FacturaResponseDTO> resultado =
                facturaService.actualizar(1L, dto);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstadoPago())
                .isEqualTo("PAGADO");
    }

    @Test
    @DisplayName("GIVEN: factura inexistente WHEN: actualizar THEN: retorna empty")
    void actualizar_shouldReturnEmpty() {

        when(pacienteClient.existePaciente(anyString()))
                .thenReturn(true);

        when(hospitalizacionClient.existeHistorial(anyLong()))
                .thenReturn(true);

        when(facturaRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<FacturaResponseDTO> resultado =
                facturaService.actualizar(99L, crearRequest());

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("GIVEN: id existente WHEN: eliminar THEN: invoca deleteById")
    void eliminar_shouldDelete() {

        facturaService.eliminar(1L);

        verify(facturaRepository).deleteById(1L);
    }
}