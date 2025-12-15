package com.idat.service;

import com.idat.model.Cliente;
import com.idat.repository.ClienteRepository;
import java.util.List;

public class ClienteService {
    
    private ClienteRepository clienteRepository;
    
    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }
    
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.obtenerTodos();
    }
    
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.obtenerPorId(id);
    }
    
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.crear(cliente);
    }
}