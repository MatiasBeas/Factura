package cl.proyecto.Factura.RepositoryTest;

import cl.proyecto.Factura.model.Factura;
import cl.proyecto.Factura.repository.FacturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - FacturaRepository")
class FacturaRepositoryTest {

    @Mock
    private FacturaRepository facturaRepository;

    private Factura crearFactura(Long id, String estado, Long codHospitalizacion) {

        Factura factura = new Factura();

        factura.setNumFactura(id);
        factura.setTotalNeto(BigDecimal.valueOf(100000));
        factura.setIva(BigDecimal.valueOf(19000));
        factura.setTotalPagar(BigDecimal.valueOf(119000));
        factura.setEstadoPago(estado);
        factura.setCodHospitalizacion(codHospitalizacion);
        factura.setPacienteRun("22.359.190-6");

        return factura;
    }

    @Test
    @DisplayName("GIVEN: facturas pagadas WHEN: buscar por estado THEN: retorna coincidencias")
    void findByEstadoPago_shouldReturnMatchingInvoices() {

        List<Factura> facturas = List.of(
                crearFactura(1L, "PAGADO", 1L),
                crearFactura(2L, "PAGADO", 2L)
        );

        when(facturaRepository.findByEstadoPago("PAGADO"))
                .thenReturn(facturas);

        List<Factura> resultado =
                facturaRepository.findByEstadoPago("PAGADO");

        assertThat(resultado).hasSize(2);
        assertThat(resultado)
                .extracting(Factura::getEstadoPago)
                .containsOnly("PAGADO");
    }

    @Test
    @DisplayName("GIVEN: estado inexistente WHEN: buscar THEN: retorna lista vacia")
    void findByEstadoPago_shouldReturnEmptyList() {

        when(facturaRepository.findByEstadoPago("ANULADA"))
                .thenReturn(List.of());

        List<Factura> resultado =
                facturaRepository.findByEstadoPago("ANULADA");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("GIVEN: hospitalizacion existente WHEN: buscar THEN: retorna coincidencias")
    void findByCodHospitalizacion_shouldReturnMatches() {

        List<Factura> facturas = List.of(
                crearFactura(1L, "PAGADO", 10L),
                crearFactura(2L, "PENDIENTE", 10L)
        );

        when(facturaRepository.findByCodHospitalizacion(10L))
                .thenReturn(facturas);

        List<Factura> resultado =
                facturaRepository.findByCodHospitalizacion(10L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado)
                .extracting(Factura::getCodHospitalizacion)
                .containsOnly(10L);
    }

    @Test
    @DisplayName("GIVEN: codigo inexistente WHEN: buscar THEN: retorna lista vacia")
    void findByCodHospitalizacion_shouldReturnEmptyList() {

        when(facturaRepository.findByCodHospitalizacion(999L))
                .thenReturn(List.of());

        List<Factura> resultado =
                facturaRepository.findByCodHospitalizacion(999L);

        assertThat(resultado).isEmpty();
    }
}