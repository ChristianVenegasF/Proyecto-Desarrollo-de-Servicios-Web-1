package com.idat.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Clientes")  // ✅ ESPECIFICAR EL NOMBRE DE COLUMNA
    private Long idClientes;  // ✅ CAMBIAR EL NOMBRE DE LA VARIABLE
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    // Constructores
    public Cliente() {
        this.fechaCreacion = new Date(); // Fecha actual
    }
    
    public Cliente(String nombre, String email, String telefono) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters CORREGIDOS
    public Long getIdClientes() { 
        return idClientes; 
    }
    
    public void setIdClientes(Long idClientes) {  // ✅ PARÁMETRO CORREGIDO
        this.idClientes = idClientes; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getTelefono() { 
        return telefono; 
    }
    
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public Date getFechaCreacion() { 
        return fechaCreacion; 
    }
    
    public void setFechaCreacion(Date fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    @Override
    public String toString() {
        return "Cliente{idClientes=" + idClientes + ", nombre='" + nombre + "', email='" + email + "', telefono='" + telefono + "', fechaCreacion=" + fechaCreacion + "}";
    }
}