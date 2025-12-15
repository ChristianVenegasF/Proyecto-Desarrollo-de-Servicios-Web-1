
# Gestión de Clientes y Citas — Servicios SOAP, API REST y Spring REST

Este repositorio agrupa tres servicios independientes que conforman el sistema **Gestión de Clientes y Citas**:

- **Servicio SOAP — Clientes** (Axis2)
- **Servicio REST — Citas** (JAX-RS/Jersey)
- **Servicio Spring REST — Clientes y Citas** (Spring MVC)

> **Objetivo:** centralizar la documentación de endpoints, cómo levantar los servicios, ejemplos de consumo y lineamientos de desarrollo.

---

## 📦 Arquitectura y Puertos

| Servicio              | Tecnología           | Puerto | Base URL                                       |
|----------------------|----------------------|--------|------------------------------------------------|
| SOAP (Clientes)      | Axis2 (SOAP)         | 8080   | `http://localhost:8080/servicio-soap-clientes/` |
| API REST (Citas)     | JAX-RS/Jersey        | 8080   | `http://localhost:8080/servicio-rest-citas/`   |
| Spring REST (Ambos)  | Spring MVC           | 8081   | `http://localhost:8081/`                       |

---

## 🔗 Endpoints

### 1) SOAP — Clientes
- **WSDL:**  
  `http://localhost:8080/servicio-soap-clientes/services/ClienteServiceSOAP?wsdl`

- **Base del servicio:**  
  `http://localhost:8080/servicio-soap-clientes/`

*(Operaciones y ejemplos SOAP igual que antes)*

---

### 2) API REST — Citas
- **Base URL:**  
  `http://localhost:8080/servicio-rest-citas/api/citas`

*(Endpoints y ejemplos igual que antes)*

---

### 3) Spring REST — Clientes y Citas
- **Clientes:** `http://localhost:8081/api/clientes`  
- **Citas:** `http://localhost:8081/api/citas`  
- **Frontend (si aplica):** `http://localhost:8081/index.html`

*(Endpoints y ejemplos igual que antes)*

---

## 🚀 Cómo levantar los servicios

### Prerrequisitos
- Java 8+
- Maven 3.8+
- Servidor de aplicaciones (Tomcat recomendado)
- Postman/SoapUI para pruebas

### SOAP (Clientes) — Axis2
11. Empaqueta el proyecto:
   ```bash
