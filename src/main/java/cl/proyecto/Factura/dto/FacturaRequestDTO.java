package cl.proyecto.Factura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para crear o actualizar una factura")
public class FacturaRequestDTO {

    @NotNull(message = "El total neto es obligatorio")
    @Schema(description = "Total neto de la factura", example = "100000")
    private BigDecimal totalNeto;

    @NotBlank(message = "El estado de pago es obligatorio")
    @Schema(description = "Estado de pago", example = "PAGADO")
    private String estadoPago;

    @NotNull(message = "El codigo de hospitalizacion es obligatorio")
    @Schema(description = "Codigo de hospitalizacion", example = "1")
    private Long codHospitalizacion;

    @NotBlank(message = "El RUN del paciente es obligatorio")
    @Schema(description = "RUN del paciente", example = "22.359.190-6")
    private String pacienteRun;

}
