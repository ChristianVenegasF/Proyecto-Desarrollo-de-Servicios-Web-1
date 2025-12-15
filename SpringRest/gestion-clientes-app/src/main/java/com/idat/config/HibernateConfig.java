package com.idat.config;

import java.util.Properties;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

import com.idat.model.Cita;
import com.idat.model.Cliente;



@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        try {
            Properties props = new Properties();
            props.load(HibernateConfig.class.getClassLoader().getResourceAsStream("db.properties"));

            Configuration cfg = new Configuration();

            cfg.setProperty(Environment.DRIVER, props.getProperty("db.driver"));
            cfg.setProperty(Environment.URL, props.getProperty("db.url"));
            cfg.setProperty(Environment.USER, props.getProperty("db.user"));
            cfg.setProperty(Environment.PASS, props.getProperty("db.password"));
            cfg.setProperty(Environment.DIALECT, props.getProperty("hibernate.dialect"));
            cfg.setProperty(Environment.SHOW_SQL, props.getProperty("hibernate.show_sql"));
            cfg.setProperty(Environment.FORMAT_SQL, props.getProperty("hibernate.format_sql"));
            cfg.setProperty(Environment.HBM2DDL_AUTO, props.getProperty("hibernate.hbm2ddl.auto"));

            // registrar entidades
            cfg.addAnnotatedClass(Cliente.class);
            cfg.addAnnotatedClass(Cita.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties()).build();

            return cfg.buildSessionFactory(serviceRegistry);

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar db.properties", e);
        }
    }
}