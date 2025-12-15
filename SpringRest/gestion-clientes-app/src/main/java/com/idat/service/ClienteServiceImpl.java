package com.idat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idat.dao.ClienteDAO;
import com.idat.dao.CitaDAO;
import com.idat.exception.ClienteConCitasException;
import com.idat.model.Cliente;
import com.idat.model.Cita;

@Service
public class ClienteServiceImpl implements ClienteService {
    
    @Autowired
    private ClienteDAO dao;
    
    @Autowired
    private CitaDAO daoCita;

    @Override
    public List<Cliente> listar() {
        return dao.findAll(); 
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return dao.existsByEmail(email);  // ✅ AGREGAR punto y coma que faltaba
    }  // ✅ AGREGAR llave de cierre que faltaba

    @Override
    public Cliente buscar(Long idClientes) {
        return dao.findById(idClientes); 
    }

    @Override
    public void guardar(Cliente cliente) {
        dao.save(cliente); 
    }

    @Override
    public void actualizar(Cliente cliente) {
        dao.update(cliente); 
    }

    @Override
    public void eliminar(Long idClientes) { 
        Cliente cliente = dao.findById(idClientes);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        // Verificar si el cliente tiene citas asociadas
        List<Cita> citas = daoCita.findByClienteId(cliente.getIdClientes());
        if (!citas.isEmpty()) {
            throw new ClienteConCitasException(
                "No puedes eliminar un cliente que tiene citas asociadas."
            );
        }

        dao.delete(cliente.getIdClientes());
    }
}