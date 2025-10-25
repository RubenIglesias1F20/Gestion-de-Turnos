package com.tailwait.gestionturnos.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TableroController {

    @MessageMapping("/actualizarTablero")
    @SendTo("/topic/tablero")
    public String procesarMensaje(String mensaje) {
        return "Servidor dice: " + mensaje;
    }
}
