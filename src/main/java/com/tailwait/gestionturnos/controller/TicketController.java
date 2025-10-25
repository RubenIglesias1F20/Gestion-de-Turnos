package com.tailwait.gestionturnos.controller;


import com.tailwait.gestionturnos.api.dto.PersonaResponseDTO;
import com.tailwait.gestionturnos.api.dto.TableroDTO;
import com.tailwait.gestionturnos.api.dto.TicketResponseDTO;
import com.tailwait.gestionturnos.model.Persona;
import com.tailwait.gestionturnos.repository.PersonaRepository;
import com.tailwait.gestionturnos.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PersonaRepository personaRepository;

    //Crear el Ticket
    @PostMapping
    public TicketResponseDTO create(@RequestBody final PersonaResponseDTO dto){
    Persona p =  personaRepository.findByDui(dto.dui()).orElseGet(()-> {
        Persona persona = new Persona();
        persona.setDui(dto.dui());
        persona.setNombre(dto.nombre());
        persona.setApellido(dto.apellido());
        return personaRepository.save(persona);
    });
   if(!dto.dui().matches("\\d{8}")){
       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DUI debe tener 8 digitos");
   }
    return ticketService.create(p);
    }
//Finaliza crear el ticket

    @GetMapping("/siguiente")
    public TicketResponseDTO llamarSiguiente(@RequestParam(name = "modulo") final int modulo){
        return ticketService.llamarSiguiente(modulo);
    }

    @PostMapping("/{id}/atendido")
    public TicketResponseDTO atendido(@PathVariable(name = "id") final Long id){
        return ticketService.atendido(id);
    }

    @GetMapping("/tablero/ultimos")
    public List<TableroDTO> ultimosLlamados(@RequestParam(name = "restriccion", defaultValue = "7") final   int restriccion){
        return ticketService.ultimosLlamados(restriccion);
    }

}
