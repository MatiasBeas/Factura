package cl.proyecto.Factura.Assemblers;

import cl.proyecto.Factura.controller.FacturaController;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FacturaAssembler implements RepresentationModelAssembler<FacturaResponseDTO, EntityModel<FacturaResponseDTO>> {

    @Override
    public EntityModel<FacturaResponseDTO> toModel(FacturaResponseDTO factura) {
        return EntityModel.of(factura,
                linkTo(methodOn(FacturaController.class)
                        .obtenerPorId(factura.getNumFactura())).withSelfRel(),
                linkTo(methodOn(FacturaController.class)
                        .obtenerTodos()).withRel("todas-las-facturas"));
    }
}