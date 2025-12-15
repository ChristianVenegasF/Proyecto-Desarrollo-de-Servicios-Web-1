package com.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.idat.dao.CitaDAO;
import com.idat.model.Cita;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaDAO dao;

    @Override
    public List<Cita> listar() { 
        return dao.findAll(); 
    }

    @Override
    public Cita buscar(Long idCitas) { 
        return dao.findById(idCitas); 
    }

    @Override
    public void guardar(Cita cita) { 
        dao.save(cita); 
    }

    @Override
    public void actualizar(Cita cita) { 
        dao.update(cita); 
    }

    @Override
    public void eliminar(Long idCitas) { 
        dao.delete(idCitas); 
    }
}