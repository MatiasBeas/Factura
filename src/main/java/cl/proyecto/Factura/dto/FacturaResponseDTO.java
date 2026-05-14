package cl.proyecto.Factura.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {

    private Long numFactura;
    private BigDecimal totalNeto;
    private BigDecimal iva;
    private BigDecimal totalPagar;
    private String estadoPago;
    private Long codHospitalizacion;

}
