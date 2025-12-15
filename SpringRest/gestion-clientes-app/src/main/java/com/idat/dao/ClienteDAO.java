package com.idat.dao;

import java.util.List;

import com.idat.model.Cliente;
import com.idat.model.Cita;

public interface ClienteDAO {

	List<Cliente> findAll();
    Cliente findById(Long idClientes);        // Cambiado Integer por Long
    Cliente findByEmail(String email);        // Nuevo método para buscar por email único
    List<Cliente> findByCitaId(Long citaId);
    boolean existsByEmail(String email);      // Para validar si email existe
    void save(Cliente cliente);
    void update(Cliente cliente);
    void delete(Long idClientes);             // Cambiado Integer por Long
}
