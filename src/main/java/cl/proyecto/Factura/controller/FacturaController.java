package cl.proyecto.Factura.controller;

import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.service.FacturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    // GET /api/facturas → 200 OK con lista
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(facturaService.obtenerTodos());
    }

    // GET /api/facturas/{id} → 200 OK o 404
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return facturaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/facturas/estado/{estadoPago}
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerPorEstado(
            @PathVariable String estadoPago) {
        return ResponseEntity.ok(facturaService.obtenerPorEstado(estadoPago));
    }

    // POST /api/facturas → 201 Created
    @PostMapping
    public ResponseEntity<FacturaResponseDTO> crear(
            @Valid @RequestBody FacturaRequestDTO dto) {
        FacturaResponseDTO response = facturaService.guardar(dto);
        return ResponseEntity.status(201).body(response);
    }

    // PUT /api/facturas/{id} → 200 OK o 404
    @PutMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacturaRequestDTO dto) {
        return facturaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/facturas/{id} → 204 o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (facturaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}