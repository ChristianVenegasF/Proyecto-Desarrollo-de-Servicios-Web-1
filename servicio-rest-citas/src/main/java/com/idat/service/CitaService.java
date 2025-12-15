package com.idat.service;

import com.idat.model.Cita;
import com.idat.repository.CitaRepository;
import java.util.List;

public class CitaService {
    
    private CitaRepository citaRepository;
    
    public CitaService() {
        this.citaRepository = new CitaRepository();
    }
    
    public List<Cita> obtenerTodasLasCitas() {
        try {
            return citaRepository.obtenerTodas();
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerTodasLasCitas: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita obtenerCitaPorId(Long id) {
        try {
            return citaRepository.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerCitaPorId: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita crearCita(Cita cita) {
        try {
            return citaRepository.crear(cita);
        } catch (Exception e) {
            System.err.println("❌ Error en crearCita: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita actualizarCita(Cita cita) {
        try {
            return citaRepository.actualizar(cita);
        } catch (Exception e) {
            System.err.println("❌ Error en actualizarCita: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean eliminarCita(Long id) {
        try {
            return citaRepository.eliminar(id);
        } catch (Exception e) {
            System.err.println("❌ Error en eliminarCita: " + e.getMessage());
            return false;
        }
    }
}