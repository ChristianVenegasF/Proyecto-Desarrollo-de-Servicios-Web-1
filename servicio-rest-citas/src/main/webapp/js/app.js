// app.js
const API_BASE = window.location.origin + '/servicio-rest-citas/api/citas';

// Cargar citas al iniciar
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Aplicación iniciada');
    cargarCitas();
    configurarFormulario();
});

function configurarFormulario() {
    document.getElementById('cita-form').addEventListener('submit', function(e) {
        e.preventDefault();
        guardarCita();
    });
}

async function cargarCitas() {
    showLoading(true);
    try {
        console.log('📋 Cargando citas...');
        const response = await fetch(API_BASE);
        
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        
        const citas = await response.json();
        console.log('✅ Citas cargadas:', citas);
        mostrarCitas(citas);
    } catch (error) {
        console.error('❌ Error al cargar citas:', error);
        mostrarError('Error al cargar citas: ' + error.message);
    } finally {
        showLoading(false);
    }
}

function mostrarCitas(citas) {
    const container = document.getElementById('citas-list');
    
    if (!citas || citas.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No hay citas registradas</div>';
        return;
    }

    const html = citas.map(cita => `
        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div class="flex-grow-1">
                        <h6 class="card-title text-primary">${escapeHtml(cita.motivo)}</h6>
                        <p class="card-text mb-1">
                            <strong>Fecha:</strong> ${formatDate(cita.fecha)} 
                            <strong>Hora:</strong> ${cita.hora}
                        </p>
                        <p class="card-text mb-1">
                            <strong>Cliente ID:</strong> ${cita.idClientes}
                        </p>
                        <small class="text-muted">Creado: ${formatDateTime(cita.fechaCreacion)}</small>
                    </div>
                    <div class="btn-group ms-3">
                        <button class="btn btn-warning btn-sm" onclick="editarCita(${cita.idCitas})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarCita(${cita.idCitas})">🗑️ Eliminar</button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');

    container.innerHTML = html;
}

async function guardarCita() {
    const citaId = document.getElementById('cita-id').value;
    const cita = {
        fecha: document.getElementById('fecha').value,
        hora: document.getElementById('hora').value + ':00', // Formato HH:MM:SS
        motivo: document.getElementById('motivo').value,
        idClientes: parseInt(document.getElementById('idClientes').value)
    };

    // Validaciones
    if (!cita.fecha || !cita.hora || !cita.motivo || !cita.idClientes) {
        mostrarError('Todos los campos son obligatorios');
        return;
    }

    if (citaId) {
        cita.idCitas = parseInt(citaId);
    }

    try {
        const url = citaId ? `${API_BASE}/${citaId}` : API_BASE;
        const method = citaId ? 'PUT' : 'POST';

        console.log('💾 Guardando cita:', cita);
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cita)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || `Error ${response.status}`);
        }

        const citaGuardada = await response.json();
        mostrarMensaje(`✅ Cita ${citaId ? 'actualizada' : 'creada'} correctamente`, 'success');
        limpiarFormulario();
        cargarCitas();
    } catch (error) {
        console.error('❌ Error al guardar cita:', error);
        mostrarError('Error al guardar cita: ' + error.message);
    }
}

async function editarCita(id) {
    try {
        console.log('✏️ Editando cita ID:', id);
        const response = await fetch(`${API_BASE}/${id}`);
        
        if (!response.ok) {
            throw new Error('Cita no encontrada');
        }
        
        const cita = await response.json();
        
        // Formatear fecha para input[type=date]
        const fechaFormateada = cita.fecha.split('T')[0];
        
        document.getElementById('cita-id').value = cita.idCitas;
        document.getElementById('fecha').value = fechaFormateada;
        document.getElementById('hora').value = cita.hora.substring(0, 5); // HH:MM
        document.getElementById('motivo').value = cita.motivo;
        document.getElementById('idClientes').value = cita.idClientes;
        
        document.getElementById('form-title').textContent = 'Editar Cita';
        document.querySelector('button[type="submit"]').textContent = 'Actualizar Cita';
        
        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
        console.error('❌ Error al cargar cita:', error);
        mostrarError('Error al cargar cita: ' + error.message);
    }
}

async function eliminarCita(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta cita?')) {
        return;
    }

    try {
        console.log('🗑️ Eliminando cita ID:', id);
        const response = await fetch(`${API_BASE}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar cita');
        }

        mostrarMensaje('✅ Cita eliminada correctamente', 'success');
        cargarCitas();
    } catch (error) {
        console.error('❌ Error al eliminar cita:', error);
        mostrarError('Error al eliminar cita: ' + error.message);
    }
}

async function buscarPorCliente() {
    const clienteId = document.getElementById('cliente-search').value;
    
    if (!clienteId) {
        mostrarError('Por favor ingresa un ID de cliente');
        return;
    }

    try {
        console.log('🔍 Buscando citas para cliente ID:', clienteId);
        const response = await fetch(`${API_BASE}/cliente/${clienteId}`);
        
        if (!response.ok) {
            throw new Error('Error al buscar citas');
        }
        
        const citas = await response.json();
        const container = document.getElementById('citas-cliente-list');
        
        if (!citas || citas.length === 0) {
            container.innerHTML = '<div class="alert alert-info">No se encontraron citas para este cliente</div>';
            return;
        }

        const html = citas.map(cita => `
            <div class="card mb-2">
                <div class="card-body py-2">
                    <h6 class="card-title mb-1 text-primary">${escapeHtml(cita.motivo)}</h6>
                    <p class="card-text mb-0 small">
                        <strong>Fecha:</strong> ${formatDate(cita.fecha)} - 
                        <strong>Hora:</strong> ${cita.hora}
                    </p>
                    <p class="card-text mb-0 small">
                        <strong>ID Cita:</strong> ${cita.idCitas}
                    </p>
                </div>
            </div>
        `).join('');

        container.innerHTML = html;
    } catch (error) {
        console.error('❌ Error al buscar citas:', error);
        mostrarError('Error al buscar citas: ' + error.message);
    }
}

function limpiarFormulario() {
    document.getElementById('cita-form').reset();
    document.getElementById('cita-id').value = '';
    document.getElementById('form-title').textContent = 'Nueva Cita';
    document.querySelector('button[type="submit"]').textContent = 'Guardar Cita';
}

function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}

function mostrarMensaje(mensaje, tipo) {
    const alertClass = tipo === 'success' ? 'alert-success' : 'alert-danger';
    const alert = document.createElement('div');
    alert.className = `alert ${alertClass} alert-dismissible fade show`;
    alert.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alert, container.firstChild);
    
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

function mostrarError(mensaje) {
    mostrarMensaje(mensaje, 'danger');
}

// Funciones utilitarias
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES');
}

function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('es-ES');
}