package com.tailwait.gestionturnos.controller;


import com.tailwait.gestionturnos.api.dto.PersonaResponseDTO;
import com.tailwait.gestionturnos.api.dto.TicketResponseDTO;
import com.tailwait.gestionturnos.model.Persona;
import com.tailwait.gestionturnos.repository.PersonaRepository;
import com.tailwait.gestionturnos.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PersonaRepository personaRepository;

    @PostMapping
    public TicketResponseDTO create(@RequestBody PersonaResponseDTO dto){
    Persona p =  personaRepository.findByDui(dto.dui()).orElseGet(()-> {
        Persona persona = new Persona();
        persona.setDui(dto.dui());
        persona.setNombre(dto.nombre());
        persona.setApellido(dto.apellido());
        return personaRepository.save(persona);
    });
    return ticketService.create(p);
    }

    @GetMapping("/siguiente")
    public TicketResponseDTO llamarSiguiente(@RequestParam(name = "modulo")int modulo){
        return ticketService.llamarSiguiente(modulo);
    }
}
