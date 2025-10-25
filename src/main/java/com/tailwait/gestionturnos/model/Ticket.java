package com.tailwait.gestionturnos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "ticket", indexes = {@Index(columnList = "estado"), @Index(columnList = "llamados")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String codigo;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private TicketEstado estado;

    private Instant llamados;

    private Instant creados;

    private Instant atendidos;

    private Integer moduloNumero;

    @ManyToOne
    private Persona persona;
}
