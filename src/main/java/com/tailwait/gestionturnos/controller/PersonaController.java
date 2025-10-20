package com.tailwait.gestionturnos.controller;
import java.util.Optional;
import com.tailwait.gestionturnos.api.dto.PersonaResponseDTO;
import com.tailwait.gestionturnos.model.Persona;
import com.tailwait.gestionturnos.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaRepository personaRepository;

    @GetMapping("/searchByDui/{dui}")
    public PersonaResponseDTO searchByDui(@PathVariable(name = "dui") final String dui) {
     if(!dui.matches( "\\d{8}")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DUI debe tener 8 digitos");
     }
        Optional<Persona> persona = personaRepository.findByDui(dui);
        if (persona.isPresent()) {
           return new PersonaResponseDTO(
                    persona.get().getDui(),
                    persona.get().getNombre(),
                    persona.get().getApellido()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro nada");
        }
    }
}
