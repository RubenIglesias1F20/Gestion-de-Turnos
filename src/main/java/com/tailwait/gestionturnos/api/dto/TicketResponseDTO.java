package com.tailwait.gestionturnos.api.dto;

import com.tailwait.gestionturnos.model.TicketEstado;

import java.time.Instant;

public record TicketResponseDTO (Long id, String codigo, String motivo, TicketEstado estado, Instant llamados ,Instant creados, Instant atendidos,
                                Integer moduloNumero,String nombreCompleto) {
}
