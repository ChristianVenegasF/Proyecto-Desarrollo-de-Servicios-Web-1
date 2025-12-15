package com.idat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idat.exception.ClienteConCitasException;
import com.idat.model.Cita;
import com.idat.service.CitaService;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    
    @Autowired
    private CitaService service;

    @GetMapping
    public ResponseEntity<List<Cita>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{idCitas}")
    public ResponseEntity<Cita> buscar(@PathVariable Long idCitas) {  // ✅ Cambiado "id" por "idCitas"
        Cita cita = service.buscar(idCitas);  // ✅ Cambiado "a" por "cita"
        if (cita == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<Cita> guardar(@RequestBody Cita cita) {
        service.guardar(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(cita);  // ✅ Cambiado "a" por "cita"
    }

    @PutMapping("/{idCitas}")
    public ResponseEntity<Void> actualizar(@PathVariable Long idCitas, @RequestBody Cita cita) {  // ✅ Cambiado "id" por "idCitas"
        Cita existente = service.buscar(idCitas);  // ✅ Cambiado "ex" por "existente"
        if (existente == null) return ResponseEntity.notFound().build();
        cita.setIdCitas(idCitas);  // ✅ Cambiado "a.setId" por "cita.setIdCitas"
        service.actualizar(cita);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idCitas}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idCitas) {  // ✅ Cambiado Integer por Long, "id" por "idCitas"
        Cita existente = service.buscar(idCitas);  // ✅ Cambiado "Autor ex" por "Cita existente"
        if (existente == null) return ResponseEntity.notFound().build();
        service.eliminar(idCitas);  // ✅ Cambiado "id" por "idCitas"
        return ResponseEntity.noContent().build();
    }
    
    // Captura de la excepción personalizada
    @ExceptionHandler(ClienteConCitasException.class)
    public ResponseEntity<String> handleClienteConCitasException(ClienteConCitasException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 400
                .body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ex.getMessage());
    }
}