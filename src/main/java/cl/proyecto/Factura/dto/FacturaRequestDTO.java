package cl.proyecto.Factura.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequestDTO {

    @NotNull(message = "El total neto es obligatorio")
    private BigDecimal totalNeto;

    @NotNull(message = "El estado de pago es obligatorio")
    private String estadoPago;

    @NotNull(message = "El codigo de hospitalizacion es obligatorio")
    private Long codHospitalizacion;

}
