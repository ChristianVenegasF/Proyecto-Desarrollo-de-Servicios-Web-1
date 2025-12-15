package com.idat.soap;

import com.idat.model.Cliente;
import com.idat.service.ClienteService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(
    name = "ClienteService", 
    targetNamespace = "http://soap.idat.com/",
    serviceName = "ClienteServiceSOAP",
    portName = "ClienteServiceSOAPPort"
)
public class ClienteServiceSOAP {
    
    private ClienteService clienteService;
    
    public ClienteServiceSOAP() {
        this.clienteService = new ClienteService();
    }
    
    @WebMethod(operationName = "crearCliente")
    public Cliente crearCliente(@WebParam(name = "cliente") Cliente cliente) {
        try {
            System.out.println("✅ SOAP - Creando cliente: " + cliente.getNombre());
            return clienteService.crearCliente(cliente);
        } catch (Exception e) {
            System.err.println("❌ SOAP - Error creando cliente: " + e.getMessage());
            throw new RuntimeException("Error en servicio SOAP - crearCliente: " + e.getMessage());
        }
    }
    
    @WebMethod(operationName = "obtenerCliente")
    public Cliente obtenerCliente(@WebParam(name = "idClientes") Long idClientes) { // ✅ CORREGIDO
        try {
            System.out.println("✅ SOAP - Obteniendo cliente ID: " + idClientes); // ✅ CORREGIDO
            Cliente cliente = clienteService.obtenerCliente(idClientes); // ✅ CORREGIDO
            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado con ID: " + idClientes); // ✅ CORREGIDO
            }
            return cliente;
        } catch (Exception e) {
            System.err.println("❌ SOAP - Error obteniendo cliente: " + e.getMessage());
            throw new RuntimeException("Error en servicio SOAP - obtenerCliente: " + e.getMessage());
        }
    }
    
    @WebMethod(operationName = "obtenerTodosClientes")
    public List<Cliente> obtenerTodosClientes() {
        try {
            System.out.println("✅ SOAP - Obteniendo todos los clientes");
            return clienteService.obtenerTodosClientes();
        } catch (Exception e) {
            System.err.println("❌ SOAP - Error obteniendo clientes: " + e.getMessage());
            throw new RuntimeException("Error en servicio SOAP - obtenerTodosClientes: " + e.getMessage());
        }
    }
    
    @WebMethod(operationName = "actualizarCliente")
    public Cliente actualizarCliente(@WebParam(name = "cliente") Cliente cliente) {
        try {
            // DEBUG DETALLADO - ACTUALIZADO
            System.out.println("🔄 SOAP - Intentando actualizar cliente:");
            System.out.println("   ID recibido: " + cliente.getIdClientes()); // ✅ CORREGIDO
            System.out.println("   Nombre recibido: " + cliente.getNombre());
            System.out.println("   Email recibido: " + cliente.getEmail());
            System.out.println("   Teléfono recibido: " + cliente.getTelefono());
            
            if (cliente.getIdClientes() == null) { // ✅ CORREGIDO
                System.err.println("❌ SOAP - ERROR: ID de cliente es NULL");
                throw new RuntimeException("ID de cliente es requerido para actualizar");
            }
            
            Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
            System.out.println("✅ SOAP - Cliente actualizado exitosamente: " + clienteActualizado.getIdClientes()); // ✅ CORREGIDO
            return clienteActualizado;
            
        } catch (Exception e) {
            System.err.println("❌ SOAP - Error actualizando cliente: " + e.getMessage());
            throw new RuntimeException("Error en servicio SOAP - actualizarCliente: " + e.getMessage());
        }
    }
    
    @WebMethod(operationName = "eliminarCliente")
    public String eliminarCliente(@WebParam(name = "idClientes") Long idClientes) { // ✅ CORREGIDO
        try {
            System.out.println("✅ SOAP - Eliminando cliente ID: " + idClientes); // ✅ CORREGIDO
            boolean eliminado = clienteService.eliminarCliente(idClientes); // ✅ CORREGIDO
            if (eliminado) {
                return "Cliente eliminado exitosamente";
            } else {
                return "Cliente no encontrado con ID: " + idClientes; // ✅ CORREGIDO
            }
        } catch (Exception e) {
            System.err.println("❌ SOAP - Error eliminando cliente: " + e.getMessage());
            throw new RuntimeException("Error en servicio SOAP - eliminarCliente: " + e.getMessage());
        }
    }
}