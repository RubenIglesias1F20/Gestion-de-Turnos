package com.tailwait.gestionturnos.service;

import com.tailwait.gestionturnos.api.dto.TableroDTO;
import com.tailwait.gestionturnos.api.dto.TicketResponseDTO;
import com.tailwait.gestionturnos.model.Persona;
import com.tailwait.gestionturnos.model.Ticket;
import com.tailwait.gestionturnos.model.TicketEstado;
import com.tailwait.gestionturnos.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    private final SimpMessagingTemplate ws;

    private final AtomicInteger contador = new AtomicInteger(10);
//CREAR TICKET
    public TicketResponseDTO create(final Persona persona) {
        char prefijo = 'A';
        while (true) {
            String codigo = prefijo + String.valueOf(contador.incrementAndGet());
            try {
                Ticket t = Ticket.builder()
                        .codigo(codigo)
                        .estado(TicketEstado.CREADO)
                        .creados(Instant.now())
                        .persona(persona)
                        .build();
                return buildTicketResponseDTO(ticketRepository.save(t));
            } catch (DataIntegrityViolationException ex) {
                // Si hay una violación de integridad, como un código duplicado, intentamos nuevamente
            }
        }
    }
//finaliza crear ticket

    //Endpoint para llamar siguiente ticket
    public TicketResponseDTO llamarSiguiente(final int moduloNUmero) {
        Ticket t = ticketRepository.findFirstByEstadoOrderByCreadosAsc(TicketEstado.CREADO).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No hay tickets en espera"));
        t.setEstado(TicketEstado.LLAMADO);
        t.setLlamados(Instant.now());
        t.setModuloNumero(moduloNUmero);
        t = ticketRepository.save(t);
        /// Hacer logica para enviar al websocket el  ticket llamado.
        TableroDTO dto = new TableroDTO(t.getCodigo(), t.getModuloNumero(), t.getPersona().getNombre() +" "+ t.getPersona().getApellido());
        ws.convertAndSend("/topic/tablero", dto);
        return buildTicketResponseDTO(t);
    }

    //Siguiente endpoint servir al cliente
    public TicketResponseDTO atendido(Long id) {
        Ticket t = ticketRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el ticket"));
        t.setEstado(TicketEstado.ATENDIDO);
        t.setAtendidos(Instant.now());
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", "refresh");
        ws.convertAndSend(":/topic/tablero", payload);

        return buildTicketResponseDTO(t);
    }

    public List<TableroDTO> ultimosLlamados(final int restriccion) {
        return ticketRepository.findTop10ByEstadoOrderByLlamadosDesc(TicketEstado.LLAMADO).stream().limit(restriccion)
                .map(t -> new TableroDTO(
                t.getCodigo(),
                t.getModuloNumero(),
                t.getPersona().getNombre() + "" + t.getPersona().getApellido())).toList();
    }

    private TicketResponseDTO buildTicketResponseDTO(final Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getCodigo(),
                ticket.getMotivo(),
                ticket.getEstado(),
                ticket.getLlamados(),
                ticket.getCreados(),
                ticket.getAtendidos(),
                ticket.getModuloNumero(),
                ticket.getPersona().getNombre() + " " + ticket.getPersona().getApellido());
    }
}
