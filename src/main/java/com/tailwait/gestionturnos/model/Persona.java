package com.tailwait.gestionturnos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona", indexes =  {@Index(columnList = "dui", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 8, nullable = false, unique = true)
    private String dui;
    private String nombre;
    private String apellido;

}
