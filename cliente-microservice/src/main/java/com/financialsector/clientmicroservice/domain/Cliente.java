package com.financialsector.clientmicroservice.domain;

import javax.persistence.*;

import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@AttributeOverride(name = "nombre", column = @Column(name = "nombre", nullable = true))
@AttributeOverride(name = "genero", column = @Column(name = "genero", nullable = true))
@AttributeOverride(name = "edad", column = @Column(name = "edad", nullable = true))
@AttributeOverride(name = "identificacion", column = @Column(name = "identificacion", nullable = true))
@AttributeOverride(name = "direccion", column = @Column(name = "direccion", nullable = true))
@AttributeOverride(name = "telefono", column = @Column(name = "telefono", nullable = true))
public class Cliente extends Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;

    private String contraseña;
    private String estado;

    public Cliente(String nombre, String genero, int edad, String identificacion, String direccion, String telefono, String contraseña, String estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.contraseña = contraseña;
        this.estado = estado;
    }
}
