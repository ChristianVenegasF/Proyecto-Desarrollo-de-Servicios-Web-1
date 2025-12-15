package com.idat.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.idat.model.Cliente;
import com.idat.model.Cita; // ✅ Solo import necesario

@Repository
public class ClienteDAOImpl implements ClienteDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Cliente> findAll() {
        Session s = sessionFactory.openSession();
        List<Cliente> list = s.createQuery("from Cliente", Cliente.class).list();
        s.close();
        return list;
    }

    @Override
    public Cliente findById(Long idClientes) {
        Session s = sessionFactory.openSession();
        Cliente cliente = s.get(Cliente.class, idClientes);
        s.close();
        return cliente;
    }

    @Override
    public Cliente findByEmail(String email) {
        Session s = sessionFactory.openSession();
        Cliente cliente = s.createQuery("from Cliente where email = :email", Cliente.class)
                          .setParameter("email", email)
                          .uniqueResult();
        s.close();
        return cliente;
    }

    @Override
    public boolean existsByEmail(String email) {
        Session s = sessionFactory.openSession();
        Long count = s.createQuery("select count(c) from Cliente c where email = :email", Long.class)
                     .setParameter("email", email)
                     .uniqueResult();
        s.close();
        return count != null && count > 0;
    }

    @Override
    public void save(Cliente cliente) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        s.save(cliente);
        tx.commit();
        s.close();
    }

    @Override
    public void update(Cliente cliente) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        s.update(cliente);
        tx.commit();
        s.close();
    }

    @Override
    public void delete(Long idClientes) {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        Cliente cliente = s.get(Cliente.class, idClientes);
        if (cliente != null) s.delete(cliente);
        tx.commit();
        s.close();
    }
    
    // OPCIÓN 1: Si realmente necesitas este método (CORREGIDO):
    @Override
    public List<Cliente> findByCitaId(Long citaId) { // ✅ Cambiado a List<Cliente>
        Session s = sessionFactory.openSession();
        List<Cliente> clientes = s.createQuery(
            "select c from Cliente c join c.citas cit where cit.idCitas = :citaId", Cliente.class) // ✅ Query corregida
            .setParameter("citaId", citaId)
            .list();
        s.close();
        return clientes;
    }
}