package com.financialsector.clientmicroservice.domain;
import javax.persistence.*;

import lombok.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Persona {

    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;

    public Persona(String nombre, String genero, int edad, String identificacion, String direccion, String telefono) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }
}
