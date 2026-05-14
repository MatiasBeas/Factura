package cl.proyecto.Factura.repository;

import cl.proyecto.Factura.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByEstadoPago(String estadoPago);
    List<Factura> findByCodHospitalizacion(Long codHospitalizacion);
}
