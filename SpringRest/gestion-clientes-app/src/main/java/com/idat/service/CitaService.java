package com.idat.service;


import java.util.List;


import com.idat.model.Cita;

public interface CitaService {

	List<Cita> listar();
	Cita buscar(Long idCitas);
    void guardar(Cita cita);
    void actualizar(Cita cita);
    void eliminar(Long idCitas);
}
