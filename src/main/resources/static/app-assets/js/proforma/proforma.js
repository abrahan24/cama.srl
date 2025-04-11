
$(document).ready(function() {
    buscarClientesPorNombres();
    $("#tipo_proforma").select2({
      placeholder: "Seleccione...",
    });

    $('#tipo_proforma').change(function() {
        var valor = $(this).val();
        const form = $('#form1');
        // Ejemplo de lógica condicional
        if(valor === 'CMO') {  //CMO Con Mano De Obra
            form.attr('action', '/RegistrarVentaClienteNuevo');
            // Ejemplo: mostrar campos adicionales
            $.ajax({
                type: "POST",
                url: "/manejarTipoProforma/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_proforma").html(response);
                    // Destruir instancias previas si las hay, y volver a inicializar
                    $("#form_proforma .select2").each(function () {
                        if ($.fn.select2 && $(this).hasClass("select2-hidden-accessible")) {
                            $(this).select2('destroy');
                        }
                        $(this).select2({
                            placeholder: "Seleccione...",
                            width: '100%'
                        });
                    });
                },
                error: function () {
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
        } else if(valor === 'SMO') {  //SMO Sin Mano De Obra
            // Ejemplo: hacer una consulta AJAX para datos del cliente
            form.attr('action', '/registrarVentaClienteAntiguo');
            $.ajax({
                type: "POST",
                url: "/manejarTipoProforma/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_proforma").html(response);
                   
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

function buscarClientesPorNombres() {
    // Selecciona el input por su ID (#projectinput1) y asigna el evento change
    $('#projectinput1').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var nombres = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!nombres || nombres.trim() === '') {
            console.log('El campo nombres está vacío');
            $('#tbodyPE').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/cagarTablaProformaPorNombreProducto/" + encodeURIComponent(nombres), // Codifica el parámetro
            success: function(response) {
                // Por ejemplo, llenar una tabla o mostrar los resultados
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tablaPE").html(response);
                    $('#tablePEE').DataTable({
                        language: {
                            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
                        },
                        scrollY: '300px',
                        scrollCollapse: true,
                        paging: false,
                        responsive: true,
                    });
                    $("#projectinput2").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tablePEE tbodyPE').empty(); // Limpiar tabla si no hay resultados
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

