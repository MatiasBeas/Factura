package cl.proyecto.Factura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos retornados de una Factura")
public class FacturaResponseDTO {

    @Schema(description = "Numero de Factura", example = "1")
    private Long numFactura;

    @Schema(description = "Total sin impuestos", example = "10000")
    private BigDecimal totalNeto;

    @Schema(description = "Impuestos Agregados", example = "0.19")
    private BigDecimal iva;

    @Schema(description = "Total a pagar con cargos agregados", example = "12000")
    private BigDecimal totalPagar;

    @Schema(description = "Estado de pago", example = "PAGADO")
    private String estadoPago;

    @Schema(description = "Codigo de Hospitalizacion", example = "1")
    private Long codHospitalizacion;

    @Schema(description = "Diagnostico segun la hospitalizacion", example = "Brazo roto")
    private String diagnostico;

    @Schema(description = "RUN del paciente", example = "22.359.190-6")
    private String pacienteRun;

    @Schema(description = "Nombre del Paciente", example = "Matias")
    private String nombrePaciente;

}
