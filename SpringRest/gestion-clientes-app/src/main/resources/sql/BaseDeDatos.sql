-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gestion_clientes;
USE gestion_clientes;

-- Tabla de clientes
CREATE TABLE clientes (
    id_Clientes BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de citas
CREATE TABLE citas (
    id_Citas BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    id_Clientes BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_Clientes) REFERENCES clientes(id_Clientes) ON DELETE CASCADE
);

-- Datos de ejemplo
INSERT INTO clientes (nombre, email, telefono) VALUES 
('Juan Pérez', 'juan.perez@email.com', '123456789'),
('María García', 'maria.garcia@email.com', '987654321'),
('Carlos López', 'carlos.lopez@email.com', '555444333');

INSERT INTO citas (fecha, hora, motivo, id_Clientes ) VALUES 
('2024-01-15', '10:30:00', 'Consulta general', 1),
('2024-01-16', '14:00:00', 'Revisión de resultados', 2),
('2024-01-17', '09:15:00', 'Control rutinario', 1);