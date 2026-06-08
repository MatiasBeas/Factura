package cl.proyecto.Factura.controller;

import cl.proyecto.Factura.Assemblers.FacturaAssembler;
import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@Tag(name = "Gestion de Facturas", description = "Endpoints para administrar las facturas del hospital")
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaAssembler facturaAssembler;

    //-----------------BUSCAR TODAS LAS FACTURAS----------
    @Operation(summary = "Obtener todas las facturas", description = "Retorna una lista completa de todas las facturas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FacturaResponseDTO>>> obtenerTodos() {
        List<EntityModel<FacturaResponseDTO>> facturas = facturaService.obtenerTodos()
                .stream()
                .map(facturaAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(facturas,
                linkTo(methodOn(FacturaController.class).obtenerTodos()).withSelfRel()));
    }

    //-----------------BUSCAR LAS FACTURAS POR ID---------
    @Operation(summary = "Obtener factura por ID", description = "Retorna una factura especifica por su numero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<FacturaResponseDTO>> obtenerPorId(
            @Parameter(description = "Numero de factura", example = "1")
            @PathVariable Long id) {
        return facturaService.obtenerPorId(id)
                .map(facturaAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //-----------------BUSCAR LAS FACTURAS POR ESTADO---------
    @Operation(summary = "Obtener facturas por estado", description = "Retorna todas las facturas con un estado de pago especifico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<FacturaResponseDTO>> obtenerPorEstado(
            @Parameter(description = "Estado de pago", example = "PAGADO")
            @PathVariable String estadoPago) {
        return ResponseEntity.ok(facturaService.obtenerPorEstado(estadoPago));
    }

    //-----------------GUARDAR FACTURA----------
    @Operation(summary = "Crear factura", description = "Crea una nueva factura en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<FacturaResponseDTO>> crear(
            @Valid @RequestBody FacturaRequestDTO dto) {
        FacturaResponseDTO response = facturaService.guardar(dto);
        return ResponseEntity.status(201).body(facturaAssembler.toModel(response));
    }

    //-----------------ACTUALIZACION FACTURA----------
    @Operation(summary = "Actualizar factura", description = "Actualiza los datos de una factura existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<FacturaResponseDTO>> actualizar(
            @Parameter(description = "Numero de factura", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody FacturaRequestDTO dto) {
        return facturaService.actualizar(id, dto)
                .map(facturaAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //-----------------ELIMINAR FACTURA----------
    @Operation(summary = "Eliminar factura", description = "Elimina una factura del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Factura eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Numero de factura", example = "1")
            @PathVariable Long id) {
        if (facturaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}