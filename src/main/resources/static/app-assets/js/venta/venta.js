
$(document).ready(function() {
    
    $('#tipo_cliente').change(function() {
        var valor = $(this).val();
        var texto = $(this).find('option:selected').text();
        const form = $('#form1');
        // Ejemplo de lógica condicional
        if(valor === 'N') {
            console.log('Acciones para cliente nuevo');
            form.attr('action', '/RegistrarVentaClienteNuevo');
            // Ejemplo: mostrar campos adicionales
            $.ajax({
                type: "POST",
                url: "/manejarTipoCliente/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_cliente").html(response);
                    $('#id_cliente').val('');
                },
                error: function () {
                    console.log("Error al cargar el Formulario Cliente Nuevo");
                    Swal.fire({
                        title: 'Su Session Expiro!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    setInterval(() => {
                        window.location.href = "/login";
                    }, 1600);
                }
            });
        } else if(valor === 'A') {
            console.log('Acciones para cliente antiguo');
            // Ejemplo: hacer una consulta AJAX para datos del cliente
            form.attr('action', '/registrarVentaClienteAntiguo');
            $.ajax({
                type: "POST",
                url: "/manejarTipoCliente/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_cliente").html(response);
                    buscarClientesPorNombres();
                    buscarClientesPorNit();
                    buscarClientesPorCI();
                    loadScripts([
                        '../../../app-assets/js/scripts/forms/validation/form-validation.js'
                    ], function() {
                        // Inicializar componentes después de cargar los scripts
                        inicializarTabla();
                        // Otras inicializaciones necesarias...
                    });
                },
                error: function () {
                    console.log("Error al cargar el Formulario Cliente Antiguo");
                    Swal.fire({
                        title: 'Su Session Expiro!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    setInterval(() => {
                        window.location.href = "/login";
                    }, 1600);
                }
            });
        } else {
            console.log('No hay selección');
            // Ejemplo: ocultar secciones
        }
    });



});


// Función para cargar múltiples scripts secuencialmente
function loadScripts(scripts, callback) {
    var loaded = 0;
    
    scripts.forEach(function(script) {
        $.getScript(script)
            .done(function() {
                console.log('Script cargado:', script);
                loaded++;
                if(loaded === scripts.length && typeof callback === 'function') {
                    callback();
                }
            })
            .fail(function(jqxhr, settings, exception) {
                console.error('Error cargando script:', script, exception);
                loaded++;
                if(loaded === scripts.length && typeof callback === 'function') {
                    callback();
                }
            });
    });
}

function buscarClientesPorNombres() {
    // Selecciona el input por su ID (#projectinput1) y asigna el evento change
    $('#projectinput1').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var nombres = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!nombres || nombres.trim() === '') {
            console.log('El campo nombres está vacío');
            $('#tbodyA').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/obtenerClientesPorNombres/" + encodeURIComponent(nombres), // Codifica el parámetro
            success: function(response) {
                // Por ejemplo, llenar una tabla o mostrar los resultados
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tbodyA").html(response);
                    $("#projectinput2").val('');
                    $("#projectinput3").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tableA tbody').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if(xhr.status === 401) {
                    Swal.fire({
                        title: 'Su sesión ha expirado!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } else {
                    Swal.fire({
                        title: 'Error en la búsqueda',
                        text: 'Ocurrió un error al realizar la búsqueda',
                        icon: 'error'
                    });
                }
            }
        });
    });
}

function buscarClientesPorNit() {
    // Selecciona el input por su ID (#projectinput1) y asigna el evento change
    $('#projectinput2').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var nit = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!nit || nit.trim() === '') {
            $('#tbodyA').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/obtenerClientesPorNit/" + encodeURIComponent(nit), // Codifica el parámetro
            success: function(response) {
                // Por ejemplo, llenar una tabla o mostrar los resultados                
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tbodyA").html(response);
                    $("#projectinput1").val('');
                    $("#projectinput3").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tableA tbody').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if(xhr.status === 401) {
                    Swal.fire({
                        title: 'Su sesión ha expirado!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } else {
                    Swal.fire({
                        title: 'Error en la búsqueda',
                        text: 'Ocurrió un error al realizar la búsqueda',
                        icon: 'error'
                    });
                }
            }
        });
    });
}
function buscarClientesPorCI() {
    // Selecciona el input por su ID (#projectinput1) y asigna el evento change
    $('#projectinput3').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var ci = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!ci || ci.trim() === '') {
            $('#tbodyA').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/obtenerClientesPorCi/" + encodeURIComponent(ci), // Codifica el parámetro
            success: function(response) {
                console.log('Respuesta recibida:', response);
                // Aquí puedes procesar la respuesta (response) que viene de tu backend
                // Por ejemplo, llenar una tabla o mostrar los resultados
                
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tbodyA").html(response);
                    $("#projectinput1").val('');
                    $("#projectinput2").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tableA tbody').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if(xhr.status === 401) {
                    Swal.fire({
                        title: 'Su sesión ha expirado!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } else {
                    Swal.fire({
                        title: 'Error en la búsqueda',
                        text: 'Ocurrió un error al realizar la búsqueda',
                        icon: 'error'
                    });
                }
            }
        });
    });
}


// Función para inicializar la tabla vacía al cargar la página
function inicializarTabla() {
    if(!$.fn.DataTable.isDataTable('#tableA')) {
        $('#tableA').DataTable({
            language: {
                "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
            },
            scrollY: '400px',
            scrollCollapse: true,
            paging: false,
            responsive: true,
        });
    }
}

function seleccionarCliente(id_cliente){
    const id = id_cliente;

    $('#id_cliente').val(id);

    Swal.fire({
        title: 'Operación Exitosa!',
        text: 'Cliente Seleccionado con Exito!',
        icon: 'success',
        showConfirmButton: true
    });
}