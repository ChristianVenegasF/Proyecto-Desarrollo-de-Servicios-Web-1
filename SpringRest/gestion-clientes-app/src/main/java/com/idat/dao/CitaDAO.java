package com.idat.dao;

import java.util.List;

import com.idat.model.Cita;

public interface CitaDAO {  // Agregar "interface"
    List<Cita> findAll();
    Cita findById(Long idCitas);  // Cambiar Integer por Long
    void save(Cita cita);
    void update(Cita cita);
    void delete(Long id);  // Cambiar Integer por Long
 
    List<Cita> findByClienteId(Long clienteId);  // ✅ Este método debe existir
}