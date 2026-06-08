package cl.proyecto.Factura.config;

import cl.proyecto.Factura.model.Factura;
import cl.proyecto.Factura.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FacturaRepository facturaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (facturaRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }

        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");

        facturaRepository.save(new Factura(null, new BigDecimal("100000"), new BigDecimal("19000"), new BigDecimal("119000"), "PAGADO",    1L, "10.333.333-3"));
        facturaRepository.save(new Factura(null, new BigDecimal("250000"), new BigDecimal("47500"), new BigDecimal("297500"), "PENDIENTE", 2L, "11.111.111-1"));
        facturaRepository.save(new Factura(null, new BigDecimal("180000"), new BigDecimal("34200"), new BigDecimal("214200"), "PAGADO",    3L, "18.765.432-1"));

        log.info(">>> DataInitializer: {} facturas insertadas correctamente.", facturaRepository.count());
    }
}