package com.tailwait.gestionturnos.repository;

import com.tailwait.gestionturnos.model.Ticket;
import com.tailwait.gestionturnos.model.TicketEstado;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findFirstByEstadoOrderByCreadosAsc(TicketEstado estado);

    List<Ticket> findTop10ByEstadoOrderByLlamadosDesc(TicketEstado estado);


}
