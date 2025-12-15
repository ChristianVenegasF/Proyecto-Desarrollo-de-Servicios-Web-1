package com.idat.repository;

import com.idat.model.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClienteRepository {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public ClienteRepository() {
        this.emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");
        this.em = emf.createEntityManager();
    }
    
    public List<Cliente> obtenerTodos() {
        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
        return query.getResultList();
    }
    
    public Cliente obtenerPorId(Long id) {
        return em.find(Cliente.class, id);
    }
    
    public Cliente crear(Cliente cliente) {
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        return cliente;
    }
    
    public void cerrar() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }
}