package com.idat.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "clientePU";
    private static EntityManagerFactory factory;
    
    public static EntityManager getEntityManager() {
        // DESCOMENTAR esto para usar BD real:
        if (factory == null) {
            try {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                System.out.println("✅ Conexión a BD establecida");
            } catch (Exception e) {
                System.err.println("❌ Error conectando a BD: " + e.getMessage());
                throw e;
            }
        }
        return factory.createEntityManager();
        
        // ELIMINAR o COMENTAR esta línea temporal:
        // System.out.println("⚠️  Modo prueba - Sin conexión a BD");
        // return null;
    }
    
    public static void close() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}