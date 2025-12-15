package com.idat.service;

import com.idat.model.Cliente;
import com.idat.repository.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClienteService {
    
    private EntityManager em;
    
    public ClienteService() {
        this.em = JPAUtil.getEntityManager();
    }
    
    // Crear cliente - debe retornar Cliente
    public Cliente crearCliente(Cliente cliente) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Verificar si el email ya existe
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
            query.setParameter("email", cliente.getEmail());
            List<Cliente> existentes = query.getResultList();
            
            if (!existentes.isEmpty()) {
                throw new RuntimeException("El email '" + cliente.getEmail() + "' ya está registrado");
            }
            
            em.persist(cliente);
            tx.commit();
            
            System.out.println("✅ Cliente creado con ID: " + cliente.getIdClientes()); // ✅ CORREGIDO
            return cliente;
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear cliente: " + e.getMessage());
        }
    }
    
    // Obtener cliente por ID
    public Cliente obtenerCliente(Long idClientes) { // ✅ CORREGIDO PARÁMETRO
        try {
            Cliente cliente = em.find(Cliente.class, idClientes);
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado con ID: " + idClientes); // ✅ CORREGIDO
            }
            return cliente;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cliente: " + e.getMessage());
        }
    }
    
    // Obtener todos los clientes
    public List<Cliente> obtenerTodosClientes() {
        try {
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.idClientes", Cliente.class); // ✅ CORREGIDO
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener clientes: " + e.getMessage());
        }
    }
    
    // ACTUALIZAR CLIENTE - Corregido para retornar Cliente
    public Cliente actualizarCliente(Cliente cliente) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Verificar que el cliente existe
            Cliente clienteExistente = em.find(Cliente.class, cliente.getIdClientes()); // ✅ CORREGIDO
            if (clienteExistente == null) {
                throw new RuntimeException("Cliente no encontrado con ID: " + cliente.getIdClientes()); // ✅ CORREGIDO
            }
            
            // Validaciones
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                throw new RuntimeException("El nombre es obligatorio");
            }
            
            if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
                throw new RuntimeException("El email es obligatorio");
            }
            
            // Verificar si el email ya existe en otro cliente
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.email = :email AND c.idClientes != :idClientes", Cliente.class); // ✅ CORREGIDO
            query.setParameter("email", cliente.getEmail());
            query.setParameter("idClientes", cliente.getIdClientes()); // ✅ CORREGIDO
            List<Cliente> clientesConEmail = query.getResultList();
            
            if (!clientesConEmail.isEmpty()) {
                throw new RuntimeException("El email '" + cliente.getEmail() + "' ya está registrado por otro cliente");
            }
            
            // Preservar la fecha de creación original
            cliente.setFechaCreacion(clienteExistente.getFechaCreacion());
            
            // Actualizar el cliente
            Cliente clienteActualizado = em.merge(cliente);
            tx.commit();
            
            System.out.println("✅ Cliente actualizado: " + clienteActualizado.getIdClientes()); // ✅ CORREGIDO
            return clienteActualizado;
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    // Eliminar cliente - debe retornar boolean
    public boolean eliminarCliente(Long idClientes) { // ✅ CORREGIDO PARÁMETRO
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Cliente cliente = em.find(Cliente.class, idClientes); // ✅ CORREGIDO
            if (cliente == null) {
                return false;
            }
            
            em.remove(cliente);
            tx.commit();
            
            System.out.println("✅ Cliente eliminado: " + idClientes); // ✅ CORREGIDO
            return true;
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage());
        }
    }
    
    // Cerrar EntityManager cuando ya no se necesite
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}