
$(document).ready(function() {
    
    $('#tipo_cliente').change(function() {
        var valor = $(this).val();
        var texto = $(this).find('option:selected').text();
        
        console.log('Valor seleccionado:', valor);
        console.log('Texto seleccionado:', texto);
        
        // Ejemplo de lógica condicional
        if(valor === 'N') {
            console.log('Acciones para cliente nuevo');
            // Ejemplo: mostrar campos adicionales
            $.ajax({
                type: "POST",
                url: "/manejarTipoCliente/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_cliente").html(response);
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
            $.ajax({
                type: "POST",
                url: "/manejarTipoCliente/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_cliente").html(response);
                    loadScripts([
                    
                        '../../../app-assets/js/scripts/forms/validation/form-validation.js'
                    ], function() {
                        // Inicializar componentes después de cargar los scripts
                        $('#tableA').DataTable();
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
            $('#camposNuevoCliente').hide();
            $('#camposClienteExistente').hide();
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

