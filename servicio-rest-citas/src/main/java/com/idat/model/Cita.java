package com.idat.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Citas")
    private Long idCitas;
    
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "hora", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date hora;
    
    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;
    
    @Column(name = "id_Clientes", nullable = false)
    private Long idClientes;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    // Constructores
    public Cita() {
        this.fechaCreacion = new Date();
    }
    
    public Cita(Date fecha, Date hora, String motivo, Long idClientes) {
        this();
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.idClientes = idClientes;
    }
    
    // Getters y Setters
    public Long getIdCitas() { return idCitas; }
    public void setIdCitas(Long idCitas) { this.idCitas = idCitas; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public Date getHora() { return hora; }
    public void setHora(Date hora) { this.hora = hora; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public Long getIdClientes() { return idClientes; }
    public void setIdClientes(Long idClientes) { this.idClientes = idClientes; }
    
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    @Override
    public String toString() {
        return "Cita{" +
                "idCitas=" + idCitas +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", motivo='" + motivo + '\'' +
                ", idClientes=" + idClientes +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}