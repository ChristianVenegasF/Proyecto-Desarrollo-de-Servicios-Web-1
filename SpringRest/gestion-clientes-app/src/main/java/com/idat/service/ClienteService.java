package com.idat.service;

import java.util.List;

import com.idat.model.Cliente;

public interface ClienteService {
	 List<Cliente> listar();
	 Cliente buscar(Long idClientes);
	  void guardar(Cliente cliente);
	  void actualizar(Cliente cliente);
	  void eliminar(Long idClientes);
	  Cliente buscarPorEmail(String email);  // ✅ AGREGAR este método
	  boolean existePorEmail(String email);  // ✅ AGREGAR este método
}
