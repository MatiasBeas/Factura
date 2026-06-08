package cl.proyecto.Factura.service;

import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.model.Factura;
import cl.proyecto.Factura.repository.FacturaRepository;
import cl.proyecto.Factura.webClient.HospitalizacionClient;
import cl.proyecto.Factura.webClient.PacienteClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PacienteClient pacienteClient;
    private final HospitalizacionClient hospitalizacionClient;

    //-----------------MAPEO PRIVADO: FACTURA -> ResponseDTO----------
    private FacturaResponseDTO mapToDTO(Factura factura) {
        String nombrePaciente = pacienteClient.obtenerNombreCompleto(factura.getPacienteRun());
        String diagnostico = hospitalizacionClient.obtenerDiagnostico(factura.getCodHospitalizacion());

        return new FacturaResponseDTO(
                factura.getNumFactura(),
                factura.getTotalNeto(),
                factura.getIva(),
                factura.getTotalPagar(),
                factura.getEstadoPago(),
                factura.getCodHospitalizacion(),
                diagnostico,
                factura.getPacienteRun(),
                nombrePaciente
        );
    }

    //-----------------BUSCAR FACTURA DE DISTINTAS FORMAS----------
    public List<FacturaResponseDTO> obtenerTodos() {
        log.info("Consultando TODAS las Facturas");
        return facturaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FacturaResponseDTO> obtenerPorId(Long id) {
        log.info("Consultando Factura con id: " + id);
        return facturaRepository.findById(id).map(this::mapToDTO);
    }

    public List<FacturaResponseDTO> obtenerPorEstado(String estadoPago) {
        log.info("Consultando Facturas con estado: " + estadoPago);
        return facturaRepository.findByEstadoPago(estadoPago)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //-----------------GUARDAR FACTURA----------
    public FacturaResponseDTO guardar(FacturaRequestDTO dto) {
        log.info("Guardando nueva Factura");

        if (!pacienteClient.existePaciente(dto.getPacienteRun())) {
            throw new RuntimeException("No existe un paciente con el RUN: " + dto.getPacienteRun());
        }

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
                dto.getCodHospitalizacion(),
                dto.getPacienteRun()
        );
        return mapToDTO(facturaRepository.save(factura));
    }

    //-----------------ACTUALIZACION FACTURA----------
    public Optional<FacturaResponseDTO> actualizar(Long id, FacturaRequestDTO dto) {
        log.info("Actualizando Factura con id: " + id);

        if (!pacienteClient.existePaciente(dto.getPacienteRun())) {
            throw new RuntimeException("No existe un paciente con el RUN: " + dto.getPacienteRun());
        }

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
            existente.setPacienteRun(dto.getPacienteRun());
            return mapToDTO(facturaRepository.save(existente));
        });
    }

    //-----------------ELIMINAR FACTURA----------
    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
        log.info("Eliminando Factura con id: " + id);
    }
}