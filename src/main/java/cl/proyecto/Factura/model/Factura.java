package cl.proyecto.Factura.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numFactura;

    @NotNull(message = "El total neto es obligatorio")
    @Column(nullable = false)
    private BigDecimal totalNeto;

    @Column(nullable = false)
    private BigDecimal iva;

    @Column(nullable = false)
    private BigDecimal totalPagar;

    @Column(nullable = false)
    private String estadoPago;

    @NotNull(message = "El codigo de hospitalizacion es obligatorio")
    @Column(nullable = false)
    private Long codHospitalizacion;

    @NotNull(message = "El rut del Paciente es obligatorio")
    @Column(nullable = false)
    private String pacienteRun;

}
