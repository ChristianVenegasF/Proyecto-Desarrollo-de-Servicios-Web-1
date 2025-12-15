package com.idat.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Citas")
    private Long idCitas;

    @Column(name = "fecha", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ ANOTACIÓN JACKSON
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    @JsonFormat(pattern = "HH:mm")  // ✅ CAMBIA A "HH:mm" (sin segundos)
    private LocalTime hora;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // ✅ ANOTACIÓN JACKSON
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)  // ✅ Carga inmediatamente el cliente
    @JoinColumn(name = "id_Clientes", nullable = false)
    private Cliente cliente;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}