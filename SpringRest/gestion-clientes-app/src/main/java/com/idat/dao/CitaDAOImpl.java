package com.idat.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.idat.model.Cita;

@Repository
public class CitaDAOImpl implements CitaDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Cita> findAll() {
        Session s = sessionFactory.openSession();
        List<Cita> list = s.createQuery("from Cita", Cita.class).list();
        s.close();
        return list;
    }

    @Override
    public Cita findById(Long idCitas) {
        Session s = sessionFactory.openSession();
        Cita cita = s.get(Cita.class, idCitas);
        s.close();
        return cita;
    }

    @Override
    public void save(Cita cita) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        s.save(cita);
        tx.commit();
        s.close();
    }

    @Override
    public void update(Cita cita) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        s.update(cita);
        tx.commit();
        s.close();
    }

    @Override
    public void delete(Long idCitas) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        Cita cita = s.get(Cita.class, idCitas);
        if (cita != null) s.delete(cita);
        tx.commit();
        s.close();
    }
    
 // ✅ IMPLEMENTACIÓN DEL MÉTODO findByClienteId
    @Override
    public List<Cita> findByClienteId(Long clienteId) {
        Session s = sessionFactory.openSession();
        List<Cita> citas = s.createQuery(
            "from Cita where cliente.idClientes = :clienteId", Cita.class)
            .setParameter("clienteId", clienteId)
            .list();
        s.close();
        return citas;
    }
}
