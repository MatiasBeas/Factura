package cl.proyecto.Factura.ControllerTest;

import cl.proyecto.Factura.Assemblers.FacturaAssembler;
import cl.proyecto.Factura.controller.FacturaController;
import cl.proyecto.Factura.dto.FacturaRequestDTO;
import cl.proyecto.Factura.dto.FacturaResponseDTO;
import cl.proyecto.Factura.service.FacturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.proyecto.Factura.security.JwtAuthFilter;
import cl.proyecto.Factura.security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacturaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests unitarios - FacturaController")
class FacturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacturaService facturaService;

    @MockitoBean
    private FacturaAssembler facturaAssembler;
    
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private FacturaResponseDTO crearResponse() {
        return new FacturaResponseDTO(
                1L,
                new BigDecimal("100000"),
                new BigDecimal("19000"),
                new BigDecimal("119000"),
                "PAGADO",
                1L,
                "Fractura",
                "22.359.190-6",
                "Juan Perez"
        );
    }

    private FacturaRequestDTO crearRequest() {
        return new FacturaRequestDTO(
                new BigDecimal("100000"),
                "PAGADO",
                1L,
                "22.359.190-6"
        );
    }

    @Test
    @DisplayName("GIVEN: facturas existentes WHEN: GET /api/facturas THEN: retorna 200")
    void obtenerTodos_shouldReturnOk() throws Exception {

        FacturaResponseDTO dto = crearResponse();

        when(facturaService.obtenerTodos())
                .thenReturn(List.of(dto));

        when(facturaAssembler.toModel(any(FacturaResponseDTO.class)))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/facturas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: factura existente WHEN: GET /api/facturas/{id} THEN: retorna 200")
    void obtenerPorId_shouldReturnOk() throws Exception {

        FacturaResponseDTO dto = crearResponse();

        when(facturaService.obtenerPorId(1L))
                .thenReturn(Optional.of(dto));

        when(facturaAssembler.toModel(dto))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/facturas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: factura inexistente WHEN: GET /api/facturas/{id} THEN: retorna 404")
    void obtenerPorId_shouldReturnNotFound() throws Exception {

        when(facturaService.obtenerPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/facturas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN: estado existente WHEN: GET /estado/{estado} THEN: retorna 200")
    void obtenerPorEstado_shouldReturnOk() throws Exception {

        when(facturaService.obtenerPorEstado("PAGADO"))
                .thenReturn(List.of(crearResponse()));

        mockMvc.perform(get("/api/facturas/estado/PAGADO"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: datos válidos WHEN: POST THEN: retorna 201")
    void crear_shouldReturnCreated() throws Exception {

        FacturaRequestDTO request = crearRequest();
        FacturaResponseDTO response = crearResponse();

        when(facturaService.guardar(any(FacturaRequestDTO.class)))
                .thenReturn(response);

        when(facturaAssembler.toModel(any(FacturaResponseDTO.class)))
                .thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GIVEN: factura existente WHEN: PUT THEN: retorna 200")
    void actualizar_shouldReturnOk() throws Exception {

        FacturaRequestDTO request = crearRequest();
        FacturaResponseDTO response = crearResponse();

        when(facturaService.actualizar(eq(1L), any(FacturaRequestDTO.class)))
                .thenReturn(Optional.of(response));

        when(facturaAssembler.toModel(any(FacturaResponseDTO.class)))
                .thenReturn(EntityModel.of(response));

        mockMvc.perform(put("/api/facturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: factura inexistente WHEN: PUT THEN: retorna 404")
    void actualizar_shouldReturnNotFound() throws Exception {

        when(facturaService.actualizar(eq(99L), any(FacturaRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/facturas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN: factura existente WHEN: DELETE THEN: retorna 204")
    void eliminar_shouldReturnNoContent() throws Exception {

        when(facturaService.obtenerPorId(1L))
                .thenReturn(Optional.of(crearResponse()));

        doNothing().when(facturaService).eliminar(1L);

        mockMvc.perform(delete("/api/facturas/1"))
                .andExpect(status().isNoContent());

        verify(facturaService).eliminar(1L);
    }

    @Test
    @DisplayName("GIVEN: factura inexistente WHEN: DELETE THEN: retorna 404")
    void eliminar_shouldReturnNotFound() throws Exception {

        when(facturaService.obtenerPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/facturas/99"))
                .andExpect(status().isNotFound());
    }
}