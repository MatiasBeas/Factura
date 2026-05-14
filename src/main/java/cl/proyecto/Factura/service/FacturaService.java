package cl.proyecto.Factura.service;

import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.model.Factura;
import cl.proyecto.Factura.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;

    // ── MAPEO PRIVADO: Entidad → ResponseDTO ─────────
    private FacturaResponseDTO mapToDTO(Factura factura) {
        return new FacturaResponseDTO(
                factura.getNumFactura(),
                factura.getTotalNeto(),
                factura.getIva(),
                factura.getTotalPagar(),
                factura.getEstadoPago(),
                factura.getCodHospitalizacion()
        );
    }

    // ── OBTENER TODOS ────────────────────────────────
    public List<FacturaResponseDTO> obtenerTodos() {
        return facturaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ── OBTENER POR ID ───────────────────────────────
    public Optional<FacturaResponseDTO> obtenerPorId(Long id) {
        return facturaRepository.findById(id).map(this::mapToDTO);
    }

    // ── OBTENER POR ESTADO ───────────────────────────
    public List<FacturaResponseDTO> obtenerPorEstado(String estadoPago) {
        return facturaRepository.findByEstadoPago(estadoPago)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FacturaResponseDTO guardar(FacturaRequestDTO dto) {
        BigDecimal iva = dto.getTotalNeto()
                .multiply(new BigDecimal("0.19"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalPagar = dto.getTotalNeto().add(iva);
        Factura factura = new Factura(
                null,
                dto.getTotalNeto(),
                iva,
                totalPagar,
                dto.getEstadoPago(),
                dto.getCodHospitalizacion()
        );
        return mapToDTO(facturaRepository.save(factura));
    }

    public Optional<FacturaResponseDTO> actualizar(Long id, FacturaRequestDTO dto) {
        return facturaRepository.findById(id).map(existente -> {
            BigDecimal iva = dto.getTotalNeto()
                    .multiply(new BigDecimal("0.19"))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal totalPagar = dto.getTotalNeto().add(iva);
            existente.setTotalNeto(dto.getTotalNeto());
            existente.setIva(iva);
            existente.setTotalPagar(totalPagar);
            existente.setEstadoPago(dto.getEstadoPago());
            existente.setCodHospitalizacion(dto.getCodHospitalizacion());
            return mapToDTO(facturaRepository.save(existente));
        });
    }

    // ── ELIMINAR ─────────────────────────────────────
    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }

}
