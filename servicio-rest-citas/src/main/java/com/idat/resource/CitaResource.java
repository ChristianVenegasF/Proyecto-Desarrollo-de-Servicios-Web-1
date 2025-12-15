package com.idat.resource;

import com.idat.model.Cita;
import com.idat.service.CitaService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {
    
    private CitaService citaService = new CitaService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // ✅ ENDPOINT DE PRUEBA
    @GET
    @Path("/test")
    public Response test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API de Citas CRUD funcionando correctamente");
        response.put("timestamp", System.currentTimeMillis());
        response.put("fecha_servidor", timestampFormat.format(new Date()));
        return Response.ok(response).build();
    }
    
    // ✅ READ - Obtener todas las citas (GET) - CORREGIDO
    @GET
    public Response getCitas() {
        try {
            System.out.println("📥 Solicitando todas las citas...");
            List<Cita> citas = citaService.obtenerTodasLasCitas();
            
            // ✅ CORRECCIÓN: Formatear fechas para respuesta segura
            List<Map<String, Object>> citasFormateadas = new java.util.ArrayList<>();
            for (Cita cita : citas) {
                Map<String, Object> citaMap = new HashMap<>();
                citaMap.put("idCitas", cita.getIdCitas());
                citaMap.put("motivo", cita.getMotivo());
                citaMap.put("idClientes", cita.getIdClientes());
                
                // Formatear fecha de manera segura
                if (cita.getFecha() != null) {
                    citaMap.put("fecha", dateFormat.format(cita.getFecha()));
                } else {
                    citaMap.put("fecha", null);
                }
                
                // Formatear hora de manera segura
                if (cita.getHora() != null) {
                    citaMap.put("hora", timeFormat.format(cita.getHora()));
                } else {
                    citaMap.put("hora", null);
                }
                
                // Formatear fecha creación
                if (cita.getFechaCreacion() != null) {
                    citaMap.put("fechaCreacion", timestampFormat.format(cita.getFechaCreacion()));
                } else {
                    citaMap.put("fechaCreacion", null);
                }
                
                citasFormateadas.add(citaMap);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("citas", citasFormateadas);
            response.put("total", citas.size());
            response.put("status", "success");
            response.put("timestamp", timestampFormat.format(new Date()));
            
            System.out.println("✅ Citas obtenidas: " + citas.size() + " registros");
            return Response.ok(response).build();
            
        } catch (Exception e) {
            System.err.println("❌ Error en getCitas: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Error al obtener citas: " + e.getMessage()))
                    .build();
        }
    }
    
    // ✅ READ - Obtener cita por ID (GET) - COMPLETAMENTE CORREGIDO
    @GET
    @Path("/{id}")
    public Response getCitaById(@PathParam("id") Long id) {
        try {
            System.out.println("📥 Solicitando cita ID: " + id);
            Cita cita = citaService.obtenerCitaPorId(id);
            
            if (cita != null) {
                // ✅ CORRECCIÓN CRÍTICA: Crear respuesta formateada de manera segura
                Map<String, Object> citaResponse = new HashMap<>();
                citaResponse.put("idCitas", cita.getIdCitas());
                citaResponse.put("motivo", cita.getMotivo());
                citaResponse.put("idClientes", cita.getIdClientes());
                
                // Manejo seguro de fecha
                if (cita.getFecha() != null) {
                    citaResponse.put("fecha", dateFormat.format(cita.getFecha()));
                    citaResponse.put("fechaOriginal", cita.getFecha().getTime()); // Timestamp para debug
                } else {
                    citaResponse.put("fecha", null);
                    citaResponse.put("fechaOriginal", null);
                }
                
                // Manejo seguro de hora
                if (cita.getHora() != null) {
                    citaResponse.put("hora", timeFormat.format(cita.getHora()));
                    citaResponse.put("horaOriginal", cita.getHora().getTime()); // Timestamp para debug
                } else {
                    citaResponse.put("hora", null);
                    citaResponse.put("horaOriginal", null);
                }
                
                // Fecha creación
                if (cita.getFechaCreacion() != null) {
                    citaResponse.put("fechaCreacion", timestampFormat.format(cita.getFechaCreacion()));
                } else {
                    citaResponse.put("fechaCreacion", null);
                }
                
                System.out.println("✅ Cita encontrada y formateada: " + citaResponse);
                return Response.ok(createSuccessResponse("Cita encontrada", citaResponse)).build();
            } else {
                System.out.println("❌ Cita no encontrada ID: " + id);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Cita con ID " + id + " no encontrada"))
                        .build();
            }
        } catch (Exception e) {
            System.err.println("❌ Error en getCitaById: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Error al obtener cita: " + e.getMessage()))
                    .build();
        }
    }
    
    // ✅ CREATE - Crear nueva cita (POST) - MEJORADO
    @POST
    public Response crearCita(Map<String, Object> citaData) {
        try {
            System.out.println("📥 Creando nueva cita con datos: " + citaData);
            
            // Validar datos requeridos
            if (!citaData.containsKey("fecha") || !citaData.containsKey("hora") || 
                !citaData.containsKey("motivo") || !citaData.containsKey("idClientes")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Faltan campos requeridos: fecha, hora, motivo, idClientes"))
                        .build();
            }
            
            String fechaStr = (String) citaData.get("fecha");
            String horaStr = (String) citaData.get("hora");
            String motivo = (String) citaData.get("motivo");
            Long idClientes = Long.valueOf(citaData.get("idClientes").toString());
            
            // ✅ CORRECCIÓN: Validar y convertir fecha/hora de forma segura
            Date fecha = null;
            Date hora = null;
            
            try {
                fecha = dateFormat.parse(fechaStr);
                System.out.println("✅ Fecha parseada: " + fecha);
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD"))
                        .build();
            }
            
            try {
                // Asegurar formato completo HH:mm:ss
                if (horaStr.length() == 5) {
                    horaStr += ":00";
                }
                hora = timeFormat.parse(horaStr);
                System.out.println("✅ Hora parseada: " + hora);
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Formato de hora inválido. Use HH:mm"))
                        .build();
            }
            
            // Crear nueva cita
            Cita nuevaCita = new Cita(fecha, hora, motivo, idClientes);
            Cita citaCreada = citaService.crearCita(nuevaCita);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cita creada exitosamente");
            response.put("id", citaCreada.getIdCitas());
            response.put("cita", citaCreada);
            response.put("timestamp", timestampFormat.format(new Date()));
            
            System.out.println("✅ Cita creada exitosamente: " + citaCreada.getIdCitas());
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (Exception e) {
            System.err.println("❌ Error en crearCita: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse("Error al crear cita: " + e.getMessage()))
                    .build();
        }
    }
    
    // ✅ UPDATE - Actualizar cita existente (PUT) - COMPLETAMENTE CORREGIDO
    @PUT
    @Path("/{id}")
    public Response actualizarCita(@PathParam("id") Long id, Map<String, Object> citaData) {
        try {
            System.out.println("📥 Actualizando cita ID: " + id + " con datos: " + citaData);
            
            // Verificar que la cita existe
            Cita citaExistente = citaService.obtenerCitaPorId(id);
            if (citaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Cita con ID " + id + " no encontrada"))
                        .build();
            }
            
            // ✅ CORRECCIÓN: Manejo robusto de fechas con validación
            if (citaData.containsKey("fecha")) {
                String fechaStr = (String) citaData.get("fecha");
                try {
                    Date fecha = dateFormat.parse(fechaStr);
                    citaExistente.setFecha(fecha);
                    System.out.println("✅ Fecha actualizada: " + fecha);
                } catch (Exception e) {
                    System.err.println("❌ Error parseando fecha: " + fechaStr);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(createErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD"))
                            .build();
                }
            }
            
            if (citaData.containsKey("hora")) {
                String horaStr = (String) citaData.get("hora");
                try {
                    // Asegurar formato completo HH:mm:ss
                    if (horaStr.length() == 5) {
                        horaStr += ":00";
                    }
                    Date hora = timeFormat.parse(horaStr);
                    citaExistente.setHora(hora);
                    System.out.println("✅ Hora actualizada: " + hora);
                } catch (Exception e) {
                    System.err.println("❌ Error parseando hora: " + horaStr);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(createErrorResponse("Formato de hora inválido. Use HH:mm"))
                            .build();
                }
            }
            
            if (citaData.containsKey("motivo")) {
                String motivo = (String) citaData.get("motivo");
                citaExistente.setMotivo(motivo);
                System.out.println("✅ Motivo actualizado: " + motivo);
            }
            
            if (citaData.containsKey("idClientes")) {
                try {
                    Long idClientes = Long.valueOf(citaData.get("idClientes").toString());
                    citaExistente.setIdClientes(idClientes);
                    System.out.println("✅ ID Cliente actualizado: " + idClientes);
                } catch (NumberFormatException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(createErrorResponse("ID de cliente inválido"))
                            .build();
                }
            }
            
            Cita citaActualizada = citaService.actualizarCita(citaExistente);
            System.out.println("✅ Cita actualizada exitosamente: " + citaActualizada);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cita actualizada exitosamente");
            response.put("cita", citaActualizada);
            response.put("timestamp", timestampFormat.format(new Date()));
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            System.err.println("❌ Error grave en actualizarCita: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Error al actualizar cita: " + e.getMessage()))
                    .build();
        }
    }
    
    // ✅ DELETE - Eliminar cita (DELETE) - MEJORADO
    @DELETE
    @Path("/{id}")
    public Response eliminarCita(@PathParam("id") Long id) {
        try {
            System.out.println("🗑️ Eliminando cita ID: " + id);
            
            // Verificar que la cita existe
            Cita citaExistente = citaService.obtenerCitaPorId(id);
            if (citaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(createErrorResponse("Cita con ID " + id + " no encontrada"))
                        .build();
            }
            
            boolean eliminado = citaService.eliminarCita(id);
            
            if (eliminado) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Cita eliminada exitosamente");
                response.put("id_eliminado", id);
                response.put("timestamp", timestampFormat.format(new Date()));
                
                System.out.println("✅ Cita eliminada exitosamente: " + id);
                return Response.ok(response).build();
            } else {
                System.err.println("❌ No se pudo eliminar cita: " + id);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(createErrorResponse("Error al eliminar cita"))
                        .build();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en eliminarCita: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Error al eliminar cita: " + e.getMessage()))
                    .build();
        }
    }
    
    // ✅ Métodos auxiliares para respuestas
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", timestampFormat.format(new Date()));
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", error);
        response.put("timestamp", timestampFormat.format(new Date()));
        return response;
    }
}