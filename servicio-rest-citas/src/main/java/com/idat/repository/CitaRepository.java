package com.idat.repository;

import com.idat.model.Cita;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class CitaRepository {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public CitaRepository() {
        this.emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");
        this.em = emf.createEntityManager();
    }
    
    public List<Cita> obtenerTodas() {
        try {
            TypedQuery<Cita> query = em.createQuery("SELECT c FROM Cita c", Cita.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerTodas: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita obtenerPorId(Long id) {
        try {
            return em.find(Cita.class, id);
        } catch (Exception e) {
            System.err.println("❌ Error en obtenerPorId: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita crear(Cita cita) {
        try {
            em.getTransaction().begin();
            em.persist(cita);
            em.getTransaction().commit();
            return cita;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error en crear: " + e.getMessage());
            throw e;
        }
    }
    
    public Cita actualizar(Cita cita) {
        try {
            em.getTransaction().begin();
            Cita citaActualizada = em.merge(cita);
            em.getTransaction().commit();
            return citaActualizada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error en actualizar: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean eliminar(Long id) {
        try {
            em.getTransaction().begin();
            Cita cita = em.find(Cita.class, id);
            if (cita != null) {
                em.remove(cita);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error en eliminar: " + e.getMessage());
            throw e;
        }
    }
    
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}