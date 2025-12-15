// URLs de la API - CAMBIA ESTO SI TU PUERTO ES DIFERENTE
const API_BASE = 'http://localhost:8081/api';
const CLIENTES_API = `${API_BASE}/clientes`;
const CITAS_API = `${API_BASE}/citas`;

// Estado global
let clientes = [];
let citas = [];
let editandoClienteId = null;
let editandoCitaId = null;

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    cargarClientes();
    cargarCitas();
    configurarEventListeners();
});

// Configurar event listeners
function configurarEventListeners() {
    // Navegación entre pestañas
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            cambiarTab(this.dataset.tab);
        });
    });

    // Formularios
    document.getElementById('formCliente').addEventListener('submit', guardarCliente);
    document.getElementById('formCita').addEventListener('submit', guardarCita);
}

// Navegación entre pestañas
function cambiarTab(tabName) {
    // Actualizar botones de navegación
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');

    // Mostrar contenido correspondiente
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.getElementById(`${tabName}-section`).classList.add('active');
}

// ========== FUNCIONES PARA CLIENTES ==========

// Cargar clientes desde la API
async function cargarClientes() {
    mostrarLoading(true);
    try {
        const response = await fetch(CLIENTES_API);
        if (!response.ok) throw new Error('Error al cargar clientes');
        
        clientes = await response.json();
        renderizarClientes();
    } catch (error) {
        mostrarNotificacion('Error al cargar clientes: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Renderizar lista de clientes
function renderizarClientes() {
    const tbody = document.getElementById('clientesBody');
    tbody.innerHTML = '';

    clientes.forEach(cliente => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${cliente.idClientes}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefono || '-'}</td>
            <td>${formatearFecha(cliente.fechaCreacion)}</td>
            <td>
                <button class="btn btn-edit" onclick="editarCliente(${cliente.idClientes})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarCliente(${cliente.idClientes})">
                    <i class="fas fa-trash"></i> Eliminar
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Abrir modal para nuevo cliente
function abrirModalCliente() {
    editandoClienteId = null;
    document.getElementById('modalClienteTitulo').textContent = 'Nuevo Cliente';
    document.getElementById('formCliente').reset();
    document.getElementById('modalCliente').style.display = 'block';
}

// Abrir modal para editar cliente
function editarCliente(id) {
    const cliente = clientes.find(c => c.idClientes === id);
    if (!cliente) return;

    editandoClienteId = id;
    document.getElementById('modalClienteTitulo').textContent = 'Editar Cliente';
    document.getElementById('clienteId').value = cliente.idClientes;
    document.getElementById('nombre').value = cliente.nombre;
    document.getElementById('email').value = cliente.email;
    document.getElementById('telefono').value = cliente.telefono || '';
    document.getElementById('modalCliente').style.display = 'block';
}

// Cerrar modal de cliente
function cerrarModalCliente() {
    document.getElementById('modalCliente').style.display = 'none';
}

// Guardar cliente (crear o actualizar)
async function guardarCliente(e) {
    e.preventDefault();
    
    const clienteData = {
        nombre: document.getElementById('nombre').value,
        email: document.getElementById('email').value,
        telefono: document.getElementById('telefono').value
    };

    mostrarLoading(true);
    try {
        let response;
        if (editandoClienteId) {
            // Actualizar cliente existente
            response = await fetch(`${CLIENTES_API}/${editandoClienteId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(clienteData)
            });
        } else {
            // Crear nuevo cliente
            response = await fetch(CLIENTES_API, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(clienteData)
            });
        }

        if (!response.ok) throw new Error('Error al guardar cliente');

        mostrarNotificacion(
            editandoClienteId ? 'Cliente actualizado correctamente' : 'Cliente creado correctamente',
            'success'
        );
        
        cerrarModalCliente();
        await cargarClientes();
        await cargarCitas(); // Recargar citas por si hay cambios en clientes
    } catch (error) {
        mostrarNotificacion('Error al guardar cliente: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}



// Eliminar cliente
async function eliminarCliente(id) {
    if (!confirm('¿Estas seguro de que quieres eliminar este cliente?')) return;

    mostrarLoading(true);
    try {
        const response = await fetch(`${CLIENTES_API}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar cliente');

        mostrarNotificacion('Cliente eliminado correctamente', 'success');
        await cargarClientes();
        await cargarCitas(); // Recargar citas por si se eliminó un cliente con citas
    } catch (error) {
        mostrarNotificacion('Error al eliminar cliente: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Buscar clientes
function buscarClientes() {
    const searchTerm = document.getElementById('searchClientes').value.toLowerCase();
    const tbody = document.getElementById('clientesBody');
    tbody.innerHTML = '';

    const clientesFiltrados = clientes.filter(cliente => 
        cliente.nombre.toLowerCase().includes(searchTerm) ||
        cliente.email.toLowerCase().includes(searchTerm) ||
        (cliente.telefono && cliente.telefono.toLowerCase().includes(searchTerm))
    );

    clientesFiltrados.forEach(cliente => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${cliente.idClientes}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefono || '-'}</td>
            <td>${formatearFecha(cliente.fechaCreacion)}</td>
            <td>
                <button class="btn btn-edit" onclick="editarCliente(${cliente.idClientes})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarCliente(${cliente.idClientes})">
                    <i class="fas fa-trash"></i> Eliminar
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// ========== FUNCIONES PARA CITAS ==========

// Guardar cita (crear o actualizar) - VERSIÓN CORREGIDA
async function guardarCita(e) {
    e.preventDefault();
    
    // ✅ AGREGAR VALIDACIONES ANTES DE CONSTRUIR EL OBJETO
    const fechaInput = document.getElementById('fecha');
    const horaInput = document.getElementById('hora');
    const motivoInput = document.getElementById('motivo');
    const clienteSelect = document.getElementById('clienteSelect');
    
    // Validar que los elementos existen
    if (!fechaInput || !horaInput || !motivoInput || !clienteSelect) {
        mostrarNotificacion('Error: Campos del formulario no encontrados', 'error');
        return;
    }
    
    const fecha = fechaInput.value;
    const hora = horaInput.value;
    const motivo = motivoInput.value.trim();
    const clienteId = parseInt(clienteSelect.value);
    
    // Validar campos vacíos
    if (!fecha || !hora || !motivo) {
        mostrarNotificacion('Por favor, completa todos los campos requeridos', 'error');
        return;
    }
    
    if (isNaN(clienteId) || clienteId <= 0) {
        mostrarNotificacion('Por favor, selecciona un cliente válido', 'error');
        return;
    }
    
    // Formatear hora si es necesario (agregar segundos)
    let horaFormateada = hora;
    if (hora.split(':').length === 2) {
        horaFormateada = hora + ':00';
    }
    
    // ✅ CONSTRUIR EL OBJETO DE MANERA SEGURA
    const citaData = {
        fecha: fecha,
        hora: horaFormateada,
        motivo: motivo,
        cliente: {
            idClientes: clienteId
        }
    };
    
    console.log('📤 Datos a enviar:', JSON.stringify(citaData, null, 2));
    
    mostrarLoading(true);
    try {
        let url = CITAS_API;
        let method = 'POST';
        
        if (editandoCitaId) {
            url = `${CITAS_API}/${editandoCitaId}`;
            method = 'PUT';
        }
        
        const response = await fetch(url, {
            method: method,
            headers: { 
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(citaData)
        });
        
        console.log('📥 Respuesta status:', response.status);
        
        if (!response.ok) {
            let errorMsg = 'Error al guardar cita (status: ' + response.status + ')';
            try {
                const errorText = await response.text();
                console.error('📄 Error texto:', errorText);
                errorMsg += ': ' + errorText;
            } catch (e) {
                console.error('❌ No se pudo leer error:', e);
            }
            throw new Error(errorMsg);
        }
        
        const responseData = await response.json();
        console.log('✅ Respuesta exitosa:', responseData);
        
        mostrarNotificacion(
            editandoCitaId ? '✅ Cita actualizada correctamente' : '✅ Cita creada correctamente',
            'success'
        );
        
        cerrarModalCita();
        await cargarCitas();
        
    } catch (error) {
        console.error('❌ Error completo:', error);
        mostrarNotificacion(`❌ Error: ${error.message}`, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Cargar citas desde la API
async function cargarCitas() {
    mostrarLoading(true);
    try {
        const response = await fetch(CITAS_API);
        if (!response.ok) throw new Error('Error al cargar citas');
        
        citas = await response.json();
        renderizarCitas();
    } catch (error) {
        mostrarNotificacion('Error al cargar citas: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Renderizar lista de citas
function renderizarCitas() {
    const tbody = document.getElementById('citasBody');
    tbody.innerHTML = '';

    citas.forEach(cita => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${cita.idCitas}</td>
            <td>${cita.fecha}</td>
            <td>${cita.hora}</td>
            <td>${cita.motivo}</td>
            <td>${cita.cliente ? cita.cliente.nombre : 'Cliente no disponible'}</td>
            <td>${formatearFecha(cita.fechaCreacion)}</td>
            <td>
                <button class="btn btn-edit" onclick="editarCita(${cita.idCitas})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarCita(${cita.idCitas})">
                    <i class="fas fa-trash"></i> Eliminar
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Abrir modal para nueva cita
async function abrirModalCita() {
    editandoCitaId = null;
    document.getElementById('modalCitaTitulo').textContent = 'Nueva Cita';
    document.getElementById('formCita').reset();
    
    // Cargar lista de clientes en el select
    await cargarClientesParaSelect();
    
    document.getElementById('modalCita').style.display = 'block';
}

// Abrir modal para editar cita
async function editarCita(id) {
    const cita = citas.find(c => c.idCitas === id);
    if (!cita) return;

    editandoCitaId = id;
    document.getElementById('modalCitaTitulo').textContent = 'Editar Cita';
    document.getElementById('citaId').value = cita.idCitas;
    document.getElementById('fecha').value = cita.fecha;
    document.getElementById('hora').value = cita.hora;
    document.getElementById('motivo').value = cita.motivo;
    
    // Cargar lista de clientes en el select
    await cargarClientesParaSelect();
    document.getElementById('clienteSelect').value = cita.cliente ? cita.cliente.idClientes : '';
    
    document.getElementById('modalCita').style.display = 'block';
}

// Cargar clientes en el select del modal de citas
async function cargarClientesParaSelect() {
    const select = document.getElementById('clienteSelect');
    select.innerHTML = '<option value="">Seleccionar cliente</option>';
    
    clientes.forEach(cliente => {
        const option = document.createElement('option');
        option.value = cliente.idClientes;
        option.textContent = `${cliente.nombre} (${cliente.email})`;
        select.appendChild(option);
    });
}

// Cerrar modal de cita
function cerrarModalCita() {
    document.getElementById('modalCita').style.display = 'none';
}

// Guardar cita (crear o actualizar)
async function guardarCita(e) {
    e.preventDefault();
    
    const citaData = {
        fecha: document.getElementById('fecha').value,
        hora: document.getElementById('hora').value,
        motivo: document.getElementById('motivo').value,
        cliente: {
            idClientes: parseInt(document.getElementById('clienteSelect').value)
        }
    };

    console.log('Datos a enviar:', citaData); // Para depurar

    mostrarLoading(true);
    try {
        let response;
        if (editandoCitaId) {
            // Actualizar cita existente
            response = await fetch(`${CITAS_API}/${editandoCitaId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(citaData)
            });
        } else {
            // Crear nueva cita
            response = await fetch(CITAS_API, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(citaData)
            });
        }

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Error del servidor:', errorText);
            throw new Error('Error al guardar cita: ' + errorText);
        }

        mostrarNotificacion(
            editandoCitaId ? 'Cita actualizada correctamente' : 'Cita creada correctamente',
            'success'
        );
        
        cerrarModalCita();
        await cargarCitas();
    } catch (error) {
        console.error('Error completo:', error);
        mostrarNotificacion('Error al guardar cita: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Eliminar cita
async function eliminarCita(id) {
    if (!confirm('¿Estás seguro de que quieres eliminar esta cita?')) return;

    mostrarLoading(true);
    try {
        const response = await fetch(`${CITAS_API}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar cita');

        mostrarNotificacion('Cita eliminada correctamente', 'success');
        await cargarCitas();
    } catch (error) {
        mostrarNotificacion('Error al eliminar cita: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

// Buscar citas
function buscarCitas() {
    const searchTerm = document.getElementById('searchCitas').value.toLowerCase();
    const tbody = document.getElementById('citasBody');
    tbody.innerHTML = '';

    const citasFiltradas = citas.filter(cita => 
        cita.motivo.toLowerCase().includes(searchTerm) ||
        (cita.cliente && cita.cliente.nombre.toLowerCase().includes(searchTerm)) ||
        cita.fecha.includes(searchTerm)
    );

    citasFiltradas.forEach(cita => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${cita.idCitas}</td>
            <td>${cita.fecha}</td>
            <td>${cita.hora}</td>
            <td>${cita.motivo}</td>
            <td>${cita.cliente ? cita.cliente.nombre : 'Cliente no disponible'}</td>
            <td>${formatearFecha(cita.fechaCreacion)}</td>
            <td>
                <button class="btn btn-edit" onclick="editarCita(${cita.idCitas})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarCita(${cita.idCitas})">
                    <i class="fas fa-trash"></i> Eliminar
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// ========== FUNCIONES UTILITARIAS ==========

// Formatear fecha para mostrar
function formatearFecha(fechaString) {
    if (!fechaString) return '-';
    const fecha = new Date(fechaString);
    return fecha.toLocaleDateString('es-ES') + ' ' + fecha.toLocaleTimeString('es-ES');
}

// Mostrar/ocultar loading
function mostrarLoading(mostrar) {
    document.getElementById('loading').style.display = mostrar ? 'flex' : 'none';
}

// Mostrar notificación
function mostrarNotificacion(mensaje, tipo) {
    const notification = document.createElement('div');
    notification.className = `notification ${tipo}`;
    notification.textContent = mensaje;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 4000);
}

// Cerrar modales al hacer click fuera
window.onclick = function(event) {
    const modalCliente = document.getElementById('modalCliente');
    const modalCita = document.getElementById('modalCita');
    
    if (event.target === modalCliente) {
        cerrarModalCliente();
    }
    if (event.target === modalCita) {
        cerrarModalCita();
    }
}

// ✅ Hacer funciones accesibles globalmente para los onclick del HTML
window.abrirModalCliente = abrirModalCliente;
window.abrirModalCita = abrirModalCita;
window.cerrarModalCliente = cerrarModalCliente;
window.cerrarModalCita = cerrarModalCita;
window.editarCliente = editarCliente;
window.eliminarCliente = eliminarCliente;
window.editarCita = editarCita;
window.eliminarCita = eliminarCita;
window.buscarClientes = buscarClientes;
window.buscarCitas = buscarCitas;